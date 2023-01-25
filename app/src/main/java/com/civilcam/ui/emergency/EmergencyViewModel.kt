package com.civilcam.ui.emergency

import android.location.Address
import android.location.Geocoder
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.civilcam.CivilcamApplication
import com.civilcam.domainLayer.EmergencyScreen
import com.civilcam.domainLayer.ServerErrors
import com.civilcam.domainLayer.model.JsonDataParser
import com.civilcam.domainLayer.model.alerts.AlertGuardianModel
import com.civilcam.domainLayer.model.user.UserState
import com.civilcam.domainLayer.serviceCast
import com.civilcam.domainLayer.usecase.alerts.GetStreamKeyUseCase
import com.civilcam.domainLayer.usecase.alerts.SendEmergencySosUseCase
import com.civilcam.domainLayer.usecase.location.FetchUserLocationUseCase
import com.civilcam.domainLayer.usecase.user.GetLocalCurrentUserUseCase
import com.civilcam.ext_features.DateUtils
import com.civilcam.ext_features.live_data.SingleLiveEvent
import com.civilcam.socket_feature.SocketHandler
import com.civilcam.socket_feature.SocketMapEvents
import com.civilcam.ui.emergency.model.*
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import java.util.*

class EmergencyViewModel(
	isVoiceActivation: Boolean?,
	private val fetchUserLocationUseCase: FetchUserLocationUseCase,
	private val getLocalCurrentUserUseCase: GetLocalCurrentUserUseCase,
	private val sendEmergencySosUseCase: SendEmergencySosUseCase,
	private val getStreamKeyUseCase: GetStreamKeyUseCase,
) : ViewModel() {

	private val _composeState: MutableStateFlow<EmergencyState> = MutableStateFlow(EmergencyState())
	val composeState: StateFlow<EmergencyState> = _composeState

	private val _steps: SingleLiveEvent<EmergencyRoute> = SingleLiveEvent()
	val steps: SingleLiveEvent<EmergencyRoute> = _steps

	private val _screenState = MutableLiveData<EmergencyScreen>()
	val screenState: LiveData<EmergencyScreen> = _screenState

	private val _currentTime = MutableLiveData<String>()
	val currentTime: LiveData<String> = _currentTime

	private val _stopStream = MutableLiveData<Unit>()
	val stopStream: LiveData<Unit> = _stopStream

	private val _controlTorch = MutableLiveData<Boolean>()
	val controlTorch: LiveData<Boolean> = _controlTorch

	private val _streamKey = Channel<String>()
	val streamKey = _streamKey.receiveAsFlow()

	private var geocoder = Geocoder(CivilcamApplication.instance, Locale.US)
	private val mSocket = SocketHandler.getSocket()
	private val gson = Gson()

	private val compositeDisposable = CompositeDisposable()

	init {
		getLocalCurrentUserUseCase().let { user ->
			if (user?.sessionUser?.userState == UserState.alert) {
				setSosState()
				getStreamKeyUseCase().takeIf { it.isNotEmpty() }
					?.let {
						Timber.tag("stream_flow").d("stream key $it")
						viewModelScope.launch {
							_streamKey.send(it)
						}
					}
			}
		}
		if (isVoiceActivation == true) {
			viewModelScope.launch {
				delay(300)
				doubleClickSos()
			}
		}
	}

	fun loadAvatar() {
		getLocalCurrentUserUseCase().let { user ->
			_composeState.update { it.copy(userAvatar = user?.userBaseInfo?.avatar) }
		}
	}

	fun fetchUserLocation() {
		if (!composeState.value.isLocationAllowed) {
			viewModelScope.launch {
				fetchUserLocationUseCase().onEach { location ->
					if (_composeState.value.emergencyButton == EmergencyButton.InDangerButton) {
						emitMsg(
							JsonDataParser(
								latitude = location.first.latitude,
								longitude = location.first.longitude
							)
						)
					}
					_composeState.update {
						it.copy(
							emergencyUserModel = it.emergencyUserModel?.copy(
								userLocation = location.first, userBearing = location.second
							) ?: EmergencyUserModel(
								userLocation = location.first,
								userBearing = location.second,
							), isLoading = false
						)
					}

					Timber.i("fetchUserLocationUseCase latlng ${location.first} bearing ${location.second}")
					val addressList: MutableList<Address>
					var address = ""
					try {
						addressList = geocoder.getFromLocation(
							location.first.latitude, location.first.longitude, 1
						)?.toMutableList() ?: mutableListOf()
						if (addressList.isNotEmpty()) address =
							addressList[0].getAddressLine(0).takeIf { it.isNotEmpty() } ?: address

					} catch (e: Exception) {

					}
					_composeState.update {
						it.copy(
							emergencyUserModel = it.emergencyUserModel?.copy(locationData = address)
						)
					}
					Timber.i("fetchUserLocationUseCase $address")
				}.launchIn(viewModelScope)
			}
		}
	}

	fun setInputActions(action: EmergencyActions) {
		when (action) {
			EmergencyActions.DoubleClickSos -> doubleClickSos()
			EmergencyActions.DisableSos -> disableSosStatus()
			EmergencyActions.GoUserProfile -> goUserProfile()
			EmergencyActions.GoSettings -> goSettings()
			EmergencyActions.OneClickSafe -> oneClickSafe()
			EmergencyActions.GoBack -> goBack()
			EmergencyActions.DetectLocation -> checkPermission()
			EmergencyActions.ClickCloseAlert -> clearErrorText()
			EmergencyActions.ChangeLiveScreen -> changeLiveScreen()
			EmergencyActions.ControlTorch -> controlTorch()
			is EmergencyActions.ClickChangeScreen -> screenChange(action.screenState)
			is EmergencyActions.ChangeCurrentTime -> changeCurrentTime(action.time)
		}
	}

	private fun changeCurrentTime(time: Long) {
		_currentTime.value = DateUtils.getFullDateAndTimeString(time)
	}

	private fun controlTorch() {
		_composeState.update { it.copy(isFlashEnabled = !_composeState.value.isFlashEnabled) }
		_controlTorch.value = _composeState.value.isFlashEnabled
	}

	private fun changeLiveScreen() {
		if (_composeState.value.emergencyScreen == EmergencyScreen.COUPLED) {
			_composeState.update { it.copy(emergencyScreen = EmergencyScreen.LIVE_EXTENDED) }
			steps.value = EmergencyRoute.ShowSystemUI
		} else if (_composeState.value.emergencyScreen == EmergencyScreen.LIVE_EXTENDED) {
			_composeState.update { it.copy(emergencyScreen = EmergencyScreen.COUPLED) }
			steps.value = EmergencyRoute.HideSystemUI
		}
		_screenState.value = _composeState.value.emergencyScreen
	}

	private fun clearErrorText() {
		_composeState.update { it.copy(errorText = "") }
	}

	private fun checkPermission() {
		Timber.i("checkPermission ")
		_steps.value = EmergencyRoute.CheckPermission(false)
	}

	private fun screenChange(newScreenState: EmergencyScreen) {
		_composeState.update { it.copy(emergencyScreen = newScreenState) }
		_screenState.value = newScreenState

		when (newScreenState) {
			EmergencyScreen.NORMAL, EmergencyScreen.COUPLED -> {
				steps.value = EmergencyRoute.HideSystemUI
			}
			EmergencyScreen.MAP_EXTENDED, EmergencyScreen.LIVE_EXTENDED -> steps.value =
				EmergencyRoute.ShowSystemUI

		}
	}

	private fun goBack() {
		_steps.value = EmergencyRoute.HideSystemUI
		_screenState.value = EmergencyScreen.COUPLED
		_composeState.update { it.copy(emergencyScreen = EmergencyScreen.COUPLED) }
	}

	private fun goSettings() {
		_steps.value = EmergencyRoute.GoSettings
	}

	private fun goUserProfile() {
		_steps.value = EmergencyRoute.GoUserProfile
	}

	private fun goPinCode() {
		_steps.value = EmergencyRoute.GoPinCode
	}

	private fun doubleClickSos() {
		_steps.value = EmergencyRoute.CheckPermission(true)
	}

	private fun oneClickSafe() {
		if (composeState.value.emergencyButton == EmergencyButton.InDangerButton) {
			goPinCode()
		}
	}


	override fun onCleared() {
		compositeDisposable.clear()
		super.onCleared()
	}

	private fun setSosState() {
		_steps.value = EmergencyRoute.IsNavBarVisible(false)
		_screenState.value = EmergencyScreen.COUPLED

		_composeState.update {
			it.copy(
				emergencyScreen = EmergencyScreen.COUPLED,
				emergencyButton = EmergencyButton.InDangerButton,
			)
		}
		Handler(Looper.getMainLooper()).postDelayed({
			fetchUserLocation()
		}, 400)
	}

	fun launchSos() {
		if (composeState.value.emergencyButton == EmergencyButton.InSafeButton) {
			_composeState.value.emergencyUserModel?.let { user ->
				viewModelScope.launch {
					kotlin.runCatching {
						sendEmergencySosUseCase(
							location = user.locationData,
							coords = user.userLocation,
						)
					}.onSuccess { response ->
						_steps.value = EmergencyRoute.IsNavBarVisible(false)
						_screenState.value = EmergencyScreen.COUPLED

						_composeState.update {
							it.copy(
								emergencyScreen = EmergencyScreen.COUPLED,
								emergencyButton = EmergencyButton.InDangerButton,
								alertInfo = response
							)
						}
						delay(100)
						response.alertModel.alertKey?.let { EmergencyRoute.GoLive(it) }
							?.let { _steps.value = it }
					}.onFailure { error ->
						error.serviceCast { msg, serverError, _ ->
							if (serverError == ServerErrors.SUBSCRIPTION_NOT_FOUND) {
								_steps.value = EmergencyRoute.SubscriptionEnd
								return@serviceCast
							}
							_composeState.update { it.copy(errorText = msg) }
						}
					}
				}
			}
		}
	}

	private fun disableSosStatus() {
		_screenState.value = EmergencyScreen.NORMAL
		_stopStream.value = Unit
		_steps.value = EmergencyRoute.HideSystemUI

		_composeState.update {
			it.copy(
				emergencyScreen = EmergencyScreen.NORMAL,
				emergencyButton = EmergencyButton.InSafeButton,
				emergencyUserModel = it.emergencyUserModel?.copy(guardsLocation = emptyList())
			)
		}
	}

	fun isLocationAllowed(isAllowed: Boolean) {
		_composeState.update { it.copy(isLocationAllowed = isAllowed) }
	}

	fun screenStateCheck() {
		_screenState.value = _composeState.value.emergencyScreen
		when (_composeState.value.emergencyScreen) {
			EmergencyScreen.NORMAL, EmergencyScreen.COUPLED -> _steps.value = EmergencyRoute.HideSystemUI
			EmergencyScreen.MAP_EXTENDED, EmergencyScreen.LIVE_EXTENDED -> _steps.value =
				EmergencyRoute.ShowSystemUI
		}
		Handler(Looper.getMainLooper()).postDelayed({
			_steps.value =
				EmergencyRoute.IsNavBarVisible(_composeState.value.emergencyButton == EmergencyButton.InSafeButton)
		}, 100)
	}

	fun addListeners() {
		mSocket.on(SocketMapEvents.INCOME_GUARDIANS.msgType) { args ->
			Timber.d("socket args $args")

			val data: JSONArray = args[0] as JSONArray
			Timber.d("socket data $data")
			val mutableGuardList = mutableListOf<AlertGuardianModel>()
			for (i in 0 until data.length()) {
				val item = (data[i] as JSONObject).toString()
				mutableGuardList.add(
					gson.fromJson(
						item, AlertGuardianModel::class.java
					)
				)
				Timber.d("socket casted ${data[i] as JSONObject}")
			}
			_composeState.update {
				it.copy(
					emergencyUserModel = it.emergencyUserModel?.copy(
						guardsLocation = mutableGuardList.toList()
					)
				)
			}
		}
	}

	private fun emitMsg(msg: JsonDataParser) {
		if (mSocket.connected()) {
			Timber.d("socket out ${JSONObject(gson.toJson(msg))}")
			mSocket.emit(SocketMapEvents.OUTCOME_COORDS.msgType, JSONObject(gson.toJson(msg)))
		} else {
			Timber.d("socket clicked connected")
			mSocket.connect()
		}
	}

	fun removeSocketListeners() {
		mSocket.off(SocketMapEvents.INCOME_GUARDIANS.msgType)
	}

	fun resumeStream() {
		viewModelScope.launch {
			_streamKey.send(getStreamKeyUseCase())
		}
	}

	companion object {
		private const val MAX_WAITING_MILLIS = 3_000L
	}

}