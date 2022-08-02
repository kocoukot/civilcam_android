package com.civilcam.ui.alerts.map

import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.domain.model.alerts.UserAlertInformation
import com.civilcam.ui.alerts.map.model.LiveMapActions
import com.civilcam.ui.alerts.map.model.LiveMapRoute
import com.civilcam.ui.alerts.map.model.LiveMapState
import com.civilcam.ui.emergency.model.EmergencyScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class LiveMapViewModel(
    userId: Int,
) : ComposeViewModel<LiveMapState, LiveMapRoute, LiveMapActions>() {
    override var _state: MutableStateFlow<LiveMapState> = MutableStateFlow(LiveMapState())

    init {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    userInformation = UserAlertInformation(
                        userId = 23123,
                        userName = "Sylvanas Windrunner",
                        userPhoneNumber = "+15675473876",
                        requestSent = "02.02.2022",
                    )
                )
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
//            LiveMapActions.ClickDetectLocation -> TODO()
            is LiveMapActions.ClickScreenChange -> screenChange(action.screenState)
        }
    }

    private fun screenChange(newScreenState: EmergencyScreen) {
        _state.update { it.copy(emergencyScreen = newScreenState) }
    }

    private fun resolverAlertAnswered(answer: Boolean) {
        if (answer) {
            //todo API resolve add
        }
        _state.update { it.copy(isResolveAlertVisible = false, isResolved = answer) }
    }

    private fun callUserPhone() {
        _steps.value = LiveMapRoute.CallUserPhone("4334234234") // todo fix
    }

    private fun callPolice() {
        _steps.value = LiveMapRoute.CallPolice
    }

    private fun resoleUserAlert() {
        _state.update { it.copy(isResolveAlertVisible = true) }
    }

    private fun goBack() {
        _steps.value = LiveMapRoute.GoBack
    }
}


    



