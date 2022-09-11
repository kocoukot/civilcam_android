package com.civilcam.ui.alerts.map

import androidx.lifecycle.viewModelScope
import com.civilcam.data.network.support.ServiceException
import com.civilcam.domainLayer.usecase.alerts.GetMapAlertUserDataUseCase
import com.civilcam.domainLayer.usecase.location.FetchUserLocationUseCase
import com.civilcam.ext_features.compose.ComposeViewModel
import com.civilcam.ui.alerts.map.model.LiveMapActions
import com.civilcam.ui.alerts.map.model.LiveMapRoute
import com.civilcam.ui.alerts.map.model.LiveMapState
import com.civilcam.ui.alerts.map.model.UserAlertLocationData
import com.civilcam.ui.emergency.model.EmergencyScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber


class LiveMapViewModel(
    userId: Int,
    private val fetchUserLocationUseCase: FetchUserLocationUseCase,
    private val getAlertUserDataUseCase: GetMapAlertUserDataUseCase
) : ComposeViewModel<LiveMapState, LiveMapRoute, LiveMapActions>() {
    override var _state: MutableStateFlow<LiveMapState> = MutableStateFlow(LiveMapState())
    private val locationScope = CoroutineScope(Dispatchers.IO)

    init {
        fetchUserLocation()
        viewModelScope.launch {
            kotlin.runCatching { getAlertUserDataUseCase(userId) }
                .onSuccess { user ->
                    _state.update { it.copy(userInformation = user) }
                }
                .onFailure { }
        }
    }

    fun fetchUserLocation() {
        if (!state.value.isLocationAllowed) {
            locationScope.launch {
                try {
                    fetchUserLocationUseCase()
                        .collect { location ->
                            Timber.i("fetchUserLocationUseCase $location")
                            _state.update {
                                it.copy(
                                    userAlertLocationData = it.userAlertLocationData?.copy(
                                        userLocation = location.first,
                                        userBearing = location.second
                                    ) ?: UserAlertLocationData(
                                        userLocation = location.first,
                                        userBearing = location.second,
                                    ),
                                    isLoading = false
                                )
                            }
                        }
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
            navigateRoute(LiveMapRoute.AlertResolved)
            //todo API resolve add
        }
    }

    fun isLocationAllowed(isAllowed: Boolean) {
        _state.update { it.copy(isLocationAllowed = isAllowed) }
    }


    private fun callUserPhone() {
        navigateRoute(LiveMapRoute.CallUserPhone("4334234234")) // todo fix
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
}


    



