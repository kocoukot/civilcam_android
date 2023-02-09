package com.civilcam.alert_feature.map

import androidx.lifecycle.viewModelScope
import com.civilcam.alert_feature.map.model.LiveMapActions
import com.civilcam.alert_feature.map.model.LiveMapRoute
import com.civilcam.alert_feature.map.model.LiveMapState
import com.civilcam.alert_feature.map.model.UserAlertLocationData
import com.civilcam.domainLayer.EmergencyScreen
import com.civilcam.domainLayer.ServiceException
import com.civilcam.domainLayer.model.JsonDataParser
import com.civilcam.domainLayer.model.OnGuardUserData
import com.civilcam.domainLayer.serviceCast
import com.civilcam.domainLayer.usecase.alerts.ResolveAlertUseCase
import com.civilcam.domainLayer.usecase.location.FetchUserLocationUseCase
import com.civilcam.ext_features.compose.ComposeViewModel
import com.civilcam.socket_feature.SocketHandler
import com.civilcam.socket_feature.SocketMapEvents
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber


class LiveMapViewModel(
    private val alertId: Int,
    private val fetchUserLocationUseCase: FetchUserLocationUseCase,
    private val resolveAlertUseCase: ResolveAlertUseCase,
) : ComposeViewModel<LiveMapState, LiveMapRoute, LiveMapActions>() {
    override var _state: MutableStateFlow<LiveMapState> = MutableStateFlow(LiveMapState())
    private val mSocket = SocketHandler.getSocket()
    private val gson = Gson()
    
    init {
        _state.update { it.copy(isLoading = true) }
    }

    private fun fetchUserLocation() {
        if (!state.value.isLocationAllowed) {
            viewModelScope.launch {
                try {
                    fetchUserLocationUseCase()
                        .onEach { location ->
                            emitMsg(
                                JsonDataParser(
                                    latitude = location.first.latitude,
                                    longitude = location.first.longitude
                                )
                            )

                            Timber.i("fetchUserLocationUseCase $location")
                            _state.update {
                                it.copy(
                                    currentUserLocationData = it.currentUserLocationData?.copy(
                                        userLocation = location.first,
                                        userBearing = location.second
                                    ) ?: UserAlertLocationData(
                                        userLocation = location.first,
                                        userBearing = location.second,
                                    ),
                                    isLoading = false
                                )
                            }
                        }.launchIn(viewModelScope)
                } catch (e: ServiceException) {
                    Timber.d("ServiceException $e")
                }
            }
        }
    }

    override fun setInputActions(action: LiveMapActions) {
        when (action) {
            LiveMapActions.ClickGoBack -> goBack()
            LiveMapActions.ClickCallUserPhone -> callUserPhone()
            LiveMapActions.ClickResolveAlert -> resoleUserAlert()
            is LiveMapActions.ClickResolveAlertAnswer -> resolverAlertAnswered(action.answer)
            LiveMapActions.ClickCallPolice -> callPolice()
            is LiveMapActions.ClickScreenChange -> screenChange(action.screenState)
            is LiveMapActions.SelectLocationPermission -> isLocationAllowed(action.isAllowed)
        }
    }

    private fun screenChange(newScreenState: EmergencyScreen) {
        _state.update { it.copy(emergencyScreen = newScreenState) }
    }

    private fun resolverAlertAnswered(answer: Boolean) {
        _state.update { it.copy(isResolveAlertVisible = false) }
        if (answer) {
            _state.value.onGuardUserInformation?.id?.let { alertIdResolve ->
                networkRequest(
                    action = {
                        _state.update { it.copy(isLoading = true) }
                        resolveAlertUseCase(alertId = alertIdResolve)
                    },
                    onSuccess = {
                        navigateRoute(LiveMapRoute.AlertResolved)
                    },
                    onFailure = { error ->
                        error.serviceCast { msg, _, _ -> _state.update { it.copy(errorText = msg) } }
                    },
                    onComplete = {
                        _state.update { it.copy(isLoading = false) }
                    },
                )
            }
        }
    }

    private fun isLocationAllowed(isAllowed: Boolean) {
        if (isAllowed) fetchUserLocation()
        _state.update { it.copy(isLocationAllowed = isAllowed) }
    }


    private fun callUserPhone() {
        _state.value.onGuardUserInformation?.person?.phone?.let { phone ->
            navigateRoute(LiveMapRoute.CallUserPhone(phone))
        }
    }

    private fun callPolice() {
        navigateRoute(LiveMapRoute.CallPolice)
    }

    private fun resoleUserAlert() {
        _state.update { it.copy(isResolveAlertVisible = true) }
    }

    private fun goBack() {
        navigateRoute(LiveMapRoute.GoBack)
    }

    override fun clearErrorText() {
        _state.update { it.copy(errorText = "") }

    }

    fun addListeners() {
        mSocket.on(SocketMapEvents.INCOME_ALERT.msgType) { args ->
            Timber.d("socket args $args")
            val data: JSONObject = args[0] as JSONObject
            Timber.d("socket data $data")
            val person = gson.fromJson((data).toString(), OnGuardUserData::class.java)

            Timber.d("socket person ${person.status}")
            when (person.status) {
                OnGuardUserData.AlertSocketStatus.active -> _state.update {
                    it.copy(
                        isLoading = false,
                        onGuardUserInformation = person.copy()
                    )
                }
                OnGuardUserData.AlertSocketStatus.resolved -> viewModelScope.launch(Dispatchers.Main) {
                    navigateRoute(LiveMapRoute.CloseAlert)
                }
                OnGuardUserData.AlertSocketStatus.deleted -> {
                    Timber.d("socket person ${person}")
                }
            }
        }
    }

    private fun emitMsg(msg: JsonDataParser) {
        if (mSocket.connected()) {
            Timber.d("socket msg out ${msg}")
            mSocket.emit(SocketMapEvents.OUTCOME_COORDS.msgType, JSONObject(gson.toJson(msg)))
        } else {
            Timber.d("socket clicked connected")
            mSocket.connect()
        }
    }

    fun removeSocketListeners() {
        mSocket.off(SocketMapEvents.INCOME_ALERT.msgType)
    }
}