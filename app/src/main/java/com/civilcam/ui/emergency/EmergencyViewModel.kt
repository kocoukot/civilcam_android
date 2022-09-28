package com.civilcam.ui.emergency

import android.location.Address
import android.location.Geocoder
import android.os.Handler
import android.os.Looper
import androidx.camera.core.CameraInfo
import androidx.camera.core.CameraSelector
import androidx.camera.core.TorchState
import androidx.lifecycle.viewModelScope
import com.civilcam.CivilcamApplication.Companion.instance
import com.civilcam.domainLayer.EmergencyScreen
import com.civilcam.domainLayer.serviceCast
import com.civilcam.domainLayer.usecase.alerts.SendEmergencySosUseCase
import com.civilcam.domainLayer.usecase.location.FetchUserLocationUseCase
import com.civilcam.domainLayer.usecase.user.GetLocalCurrentUserUseCase
import com.civilcam.ext_features.compose.ComposeViewModel
import com.civilcam.service.socket.SocketHandler
import com.civilcam.ui.emergency.model.*
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import java.util.*

class EmergencyViewModel(
	private val fetchUserLocationUseCase: FetchUserLocationUseCase,
	getLocalCurrentUserUseCase: GetLocalCurrentUserUseCase,
	private val sendEmergencySosUseCase: SendEmergencySosUseCase,
//	private val setUserCoordsUseCase: SetUserCoordsUseCase
) : ComposeViewModel<EmergencyState, EmergencyRoute, EmergencyActions>() {
	override var _state: MutableStateFlow<EmergencyState> = MutableStateFlow(EmergencyState())
	private var geocoder = Geocoder(instance, Locale.US)
	private val _effect = MutableSharedFlow<CameraEffect>()
	val effect: SharedFlow<CameraEffect> = _effect

	private val mSocket = SocketHandler.getSocket()


	private val locationScope = CoroutineScope(Dispatchers.IO)

	init {
		addListeners()
		getLocalCurrentUserUseCase().let { user ->
			_state.update { it.copy(userAvatar = user.userBaseInfo.avatar) }
//			when (user.sessionUser.userState) {
//				UserState.ALERT -> {
//					navigateRoute(EmergencyRoute.CheckPermission(true))
//				}
//				UserState.SAFE -> {
//				}
//			}
		}
		//	fetchUserLocation()
	}

	fun fetchUserLocation() {
		if (!state.value.isLocationAllowed) {
			locationScope.launch {
				fetchUserLocationUseCase()
					.collect { location ->
						when (_state.value.emergencyButton) {
							EmergencyButton.InSafeButton -> {
								_state.update {
									it.copy(
										emergencyUserModel = it.emergencyUserModel?.copy(
											userLocation = location.first,
											userBearing = location.second
										) ?: EmergencyUserModel(
											userLocation = location.first,
											userBearing = location.second,
										),
										isLoading = false
									)
								}
							}
							EmergencyButton.InDangerButton -> {
								val msg = mapOf(
									"latitude" to location.first.latitude,
									"longitude" to location.first.longitude,
								)
								val jsonMsg = JSONObject(msg)

								emitMsg(jsonMsg)
							}
						}

						Timber.i("fetchUserLocationUseCase latlng ${location.first} bearing ${location.second}")
						val addressList: MutableList<Address>
						var address = ""
						try {
							addressList = geocoder.getFromLocation(
								location.first.latitude,
								location.first.longitude,
								1
                            )?.toMutableList() ?: mutableListOf()
							if (addressList.isNotEmpty())
								address =
									addressList[0].getAddressLine(0).takeIf { it.isNotEmpty() }
										?: address

						} catch (e: Exception) {

						}
						_state.update {
							it.copy(
								emergencyUserModel = it.emergencyUserModel?.copy(locationData = address)
							)
						}
						Timber.i("fetchUserLocationUseCase $address")
					}
			}
		}
	}

	override fun setInputActions(action: EmergencyActions) {
		when (action) {
			EmergencyActions.DoubleClickSos -> doubleClickSos()
			EmergencyActions.DisableSos -> disableSosStatus()
			EmergencyActions.GoUserProfile -> goUserProfile()
			EmergencyActions.GoSettings -> goSettings()
			EmergencyActions.OneClickSafe -> oneClickSafe()
			EmergencyActions.GoBack -> goBack()
			EmergencyActions.ControlFlash -> onFlashTapped()
			EmergencyActions.ChangeCamera -> onCameraFlip()
			is EmergencyActions.ClickChangeScreen -> screenChange(action.screenState)
			is EmergencyActions.CameraInitialized -> onCameraInitialized(action.cameraLensInfo)
			EmergencyActions.DetectLocation -> checkPermission()
			EmergencyActions.ClickCloseAlert -> clearErrorText()
		}
	}

	private fun checkPermission() {
		Timber.i("checkPermission ")
		navigateRoute(EmergencyRoute.CheckPermission(false))
	}

	private fun screenChange(newScreenState: EmergencyScreen) {
		Timber.d("changeMode $newScreenState")
		_state.update { it.copy(emergencyScreen = newScreenState) }
		when (newScreenState) {
			EmergencyScreen.NORMAL, EmergencyScreen.COUPLED -> {
				navigateRoute(EmergencyRoute.HideSystemUI)
			}
			EmergencyScreen.MAP_EXTENDED, EmergencyScreen.LIVE_EXTENDED ->
				navigateRoute(EmergencyRoute.ShowSystemUI)
		}
	}

	private fun goBack() {
		navigateRoute(EmergencyRoute.HideSystemUI)
		_state.update { it.copy(emergencyScreen = EmergencyScreen.COUPLED) }
	}

	private fun goSettings() {
		navigateRoute(EmergencyRoute.GoSettings)
	}

	private fun goUserProfile() {
		navigateRoute(EmergencyRoute.GoUserProfile)
	}

	private fun goPinCode() {
		navigateRoute(EmergencyRoute.GoPinCode)
	}

	private fun doubleClickSos() {
		navigateRoute(EmergencyRoute.CheckPermission(true))
	}

	private fun oneClickSafe() {
		if (state.value.emergencyButton == EmergencyButton.InDangerButton) {
			goPinCode()
		} else {

		}
	}

	fun launchSos() {
		if (state.value.emergencyButton == EmergencyButton.InSafeButton) {
			_state.value.emergencyUserModel?.let { user ->
				networkRequest(
					action = {
						sendEmergencySosUseCase(
							location = user.locationData,
							coords = user.userLocation,
						)
					},
					onSuccess = {
						navigateRoute(EmergencyRoute.IsNavBarVisible(false))
						_state.update {
							it.copy(
								emergencyScreen = EmergencyScreen.COUPLED,
								emergencyButton = EmergencyButton.InDangerButton,
								emergencyUserModel = it.emergencyUserModel?.copy(
									guardsLocation = listOf(
										LatLng(41.950188, -87.780036),
										LatLng(41.852063, -87.679099),
										LatLng(41.737393, -87.772483),
									)
								)
							)
						}
					},
					onFailure = { error ->
						error.serviceCast { msg, _, isForceLogout ->
							_state.update { it.copy(errorText = msg) }
						}
					},
					onComplete = {},
				)
			}
		}
	}

	private fun disableSosStatus() {
		navigateRoute(EmergencyRoute.HideSystemUI)
		_state.update {
			it.copy(
				emergencyScreen = EmergencyScreen.NORMAL,
				emergencyButton = EmergencyButton.InSafeButton,
				emergencyUserModel = it.emergencyUserModel?.copy(guardsLocation = emptyList())
			)
		}
	}

	fun isLocationAllowed(isAllowed: Boolean) {
		_state.update { it.copy(isLocationAllowed = isAllowed) }
	}

	private fun onStopTapped() {
		viewModelScope.launch {
			_effect.emit(CameraEffect.StopRecording)
		}
	}

	private fun onRecordStarted() {
		viewModelScope.launch {
			try {
				//val filePath = fileManager.createFile("videos", "mp4")
				_effect.emit(CameraEffect.RecordVideo("filePath"))
			} catch (exception: IllegalArgumentException) {
				Timber.e(exception)
			}
		}
	}

	private fun onCameraInitialized(cameraLensInfo: HashMap<Int, CameraInfo>) {
		if (cameraLensInfo.isNotEmpty()) {
			val defaultLens = if (cameraLensInfo[CameraSelector.LENS_FACING_BACK] != null) {
				CameraSelector.LENS_FACING_BACK
			} else if (cameraLensInfo[CameraSelector.LENS_FACING_BACK] != null) {
				CameraSelector.LENS_FACING_FRONT
			} else {
				null
			}
			_state.update {
				it.copy(
					lens = it.lens ?: defaultLens,
					lensInfo = cameraLensInfo
				)
			}
		}
	}

	private fun onCameraFlip() {
		val lens = if (_state.value.lens == CameraSelector.LENS_FACING_FRONT) {
			CameraSelector.LENS_FACING_BACK
		} else {
			CameraSelector.LENS_FACING_FRONT
		}
		_state.update { it.copy(lens = lens) }
	}

	private fun onFlashTapped() {
		_state.update {
			when (_state.value.torchState) {
				TorchState.OFF -> it.copy(torchState = TorchState.ON)
				TorchState.ON -> it.copy(torchState = TorchState.OFF)
				else -> it.copy(torchState = TorchState.OFF)
			}
		}
    }

    fun screenStateCheck() {
		when (_state.value.emergencyScreen) {
			EmergencyScreen.NORMAL, EmergencyScreen.COUPLED -> navigateRoute(EmergencyRoute.HideSystemUI)
			EmergencyScreen.MAP_EXTENDED, EmergencyScreen.LIVE_EXTENDED -> navigateRoute(
				EmergencyRoute.ShowSystemUI
			)
		}
		Handler(Looper.getMainLooper()).postDelayed({
			navigateRoute(
				EmergencyRoute.IsNavBarVisible(_state.value.emergencyButton == EmergencyButton.InSafeButton)
			)
		}, 100)
	}

	override fun clearErrorText() {
		_state.update { it.copy(errorText = "") }
	}

	private fun addListeners() {
		mSocket.on("guardians") { args ->
			Timber.d("socket args $args")

			//	val data: List<JSONObject> = args[0] as List<JSONObject>

			viewModelScope.launch {
			}
		}
	}

	private fun emitMsg(msg: JSONObject) {
		if (mSocket.connected()) {
			Timber.d("socket msg out $msg")
			mSocket.emit("coords", msg, false)
		} else {
			Timber.d("socket clicked connected")
			mSocket.connect()
		}
	}
}