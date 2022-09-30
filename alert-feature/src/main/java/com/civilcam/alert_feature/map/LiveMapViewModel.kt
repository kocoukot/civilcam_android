package com.civilcam.alert_feature.map

import androidx.lifecycle.viewModelScope
import com.civilcam.alert_feature.map.model.*
import com.civilcam.domainLayer.EmergencyScreen
import com.civilcam.domainLayer.ServiceException
import com.civilcam.domainLayer.serviceCast
import com.civilcam.domainLayer.usecase.alerts.GetMapAlertUserDataUseCase
import com.civilcam.domainLayer.usecase.alerts.ResolveAlertUseCase
import com.civilcam.domainLayer.usecase.location.FetchUserLocationUseCase
import com.civilcam.ext_features.compose.ComposeViewModel
import com.google.gson.Gson
import com.test.socket_feature.SocketHandler
import com.test.socket_feature.SocketMapEvents
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
    private val getAlertUserDataUseCase: GetMapAlertUserDataUseCase,
    private val resolveAlertUseCase: ResolveAlertUseCase,
) : ComposeViewModel<LiveMapState, LiveMapRoute, LiveMapActions>() {
    override var _state: MutableStateFlow<LiveMapState> = MutableStateFlow(LiveMapState())
    private val mSocket = SocketHandler.getSocket()
    private val gson = Gson()

    fun fetchUserLocation() {
        if (!state.value.isLocationAllowed) {
            viewModelScope.launch {
                try {
                    fetchUserLocationUseCase()
                        .onEach { location ->
                            val msg = mapOf(
                                "latitude" to location.first.latitude,
                                "longitude" to location.first.longitude,
                            )
                            val jsonMsg = JSONObject(msg)

                            emitMsg(jsonMsg)

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
            LiveMapActions.ClickDetectLocation -> checkPermission()
        }
    }

    private fun checkPermission() {
        navigateRoute(LiveMapRoute.CheckPermission)
    }

    private fun screenChange(newScreenState: EmergencyScreen) {
        _state.update { it.copy(emergencyScreen = newScreenState) }
    }

    private fun resolverAlertAnswered(answer: Boolean) {
        _state.update { it.copy(isResolveAlertVisible = false) }
        if (answer) {
            networkRequest(
                action = {
                    _state.update { it.copy(isLoading = true) }
                    resolveAlertUseCase(alertId = alertId)
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

    fun isLocationAllowed(isAllowed: Boolean) {
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

    }

    fun addListeners() {

        mSocket.on(SocketMapEvents.INCOME_ALERT.msgType) { args ->
            Timber.d("socket args $args")
            val data: JSONObject = args[0] as JSONObject
            Timber.d("socket data $data")
            val person = gson.fromJson(
                (data).toString(),
                OnGuardUserData::class.java
            )
            Timber.d("socket person ${person}")

            _state.update { it.copy(onGuardUserInformation = person) }
        }
    }

    private fun emitMsg(msg: JSONObject) {
        if (mSocket.connected()) {
            Timber.d("socket msg out $msg")
            mSocket.emit(SocketMapEvents.OUTCOME_COORDS.msgType, msg.toString(), false)
        } else {
            Timber.d("socket clicked connected")
            mSocket.connect()
        }
    }

    fun removeSocketListeners() {
        mSocket.off(SocketMapEvents.INCOME_ALERT.msgType)
    }
}