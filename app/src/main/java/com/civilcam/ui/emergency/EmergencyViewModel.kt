package com.civilcam.ui.emergency

import androidx.camera.core.CameraSelector
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.ui.emergency.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber


class EmergencyViewModel : ComposeViewModel<EmergencyState, EmergencyRoute, EmergencyActions>() {
    override var _state: MutableStateFlow<EmergencyState> = MutableStateFlow(EmergencyState())

    override fun setInputActions(action: EmergencyActions) {
        when (action) {
            EmergencyActions.DoubleClickSos -> doubleClickSos()
            EmergencyActions.DisableSos -> disableSosStatus()
            EmergencyActions.GoUserProfile -> goUserProfile()
            EmergencyActions.GoSettings -> goSettings()
            EmergencyActions.OneClickSafe -> oneClickSafe()
            EmergencyActions.GoBack -> goBack()
            EmergencyActions.ControlFlash -> controlFlash()
            EmergencyActions.ChangeCamera -> changeCamera()
            is EmergencyActions.ChangeMode -> changeMode(action.mode)
            is EmergencyActions.ClickChangeScreen -> screenChange(action.screenState)
//            EmergencyActions.DetectLocation -> TODO()
        }
    }

    private fun screenChange(newScreenState: EmergencyScreen) {
        _state.update { it.copy(emergencyScreen = newScreenState) }
    }

    private fun changeCamera() {
        if (_state.value.cameraState == CameraSelector.LENS_FACING_BACK) {
            _state.value = _state.value.copy(cameraState = CameraSelector.LENS_FACING_FRONT)
        } else {
            _state.value = _state.value.copy(cameraState = CameraSelector.LENS_FACING_BACK)
        }
    }

    private fun controlFlash() {
        navigateRoute(
            EmergencyRoute.ControlFlash(
                _state.value.cameraFlash,
                _state.value.cameraState
            )
        )
        _state.value = _state.value.copy(cameraFlash = !_state.value.cameraFlash)
    }

    private fun changeMode(screen: EmergencyScreen) {
        Timber.d("changeMode $screen")
        _state.value = _state.value.copy(emergencyScreen = screen)
        steps.value = EmergencyRoute.IsNavBarVisible(screen == EmergencyScreen.NORMAL)
        when (screen) {
            EmergencyScreen.NORMAL,
            EmergencyScreen.COUPLED -> navigateRoute(EmergencyRoute.HideSystemUI)
            EmergencyScreen.MAP_EXTENDED, EmergencyScreen.LIVE_EXTENDED -> navigateRoute(
                EmergencyRoute.ShowSystemUI
            )
        }
    }

    private fun goBack() {
        navigateRoute(EmergencyRoute.HideSystemUI)
        _state.value = _state.value.copy(emergencyScreen = EmergencyScreen.COUPLED)
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

    private fun oneClickSafe() {
        if (state.value.emergencyButton == EmergencyButton.InDangerButton) {
            goPinCode()
        }
    }

    private fun doubleClickSos() {
        navigateRoute(EmergencyRoute.CheckPermission)
    }

    fun launchSos() {
        steps.value = EmergencyRoute.IsNavBarVisible(false)
        if (state.value.emergencyButton == EmergencyButton.InSafeButton) {
            _state.value = _state.value.copy(
                emergencyScreen = EmergencyScreen.COUPLED,
                emergencyButton = EmergencyButton.InDangerButton
            )
        }
    }

    private fun disableSosStatus() {
        _state.value = _state.value.copy(
            emergencyScreen = EmergencyScreen.NORMAL,
            emergencyButton = EmergencyButton.InSafeButton
        )
    }

    fun isLocationAllowed(isAllowed: Boolean) {
        _state.update { it.copy(isLocationAllowed = isAllowed) }
    }
}


    



