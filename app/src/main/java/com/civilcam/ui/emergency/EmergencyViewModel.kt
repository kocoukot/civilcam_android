package com.civilcam.ui.emergency

import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.ui.emergency.model.*
import kotlinx.coroutines.flow.MutableStateFlow


class EmergencyViewModel : ComposeViewModel<EmergencyState, EmergencyRoute, EmergencyActions>() {
    override var _state: MutableStateFlow<EmergencyState> = MutableStateFlow(EmergencyState())

    override fun setInputActions(action: EmergencyActions) {
        when (action) {
            EmergencyActions.DoubleClickSos -> doubleClickSos()
            EmergencyActions.GoUserProfile -> goUserProfile()
            EmergencyActions.GoSettings -> goSettings()
            EmergencyActions.OneClickSafe -> oneClickSafe()
            EmergencyActions.GoBack -> goBack()
            is EmergencyActions.ChangeMode -> changeMode(action.mode)
        }
    }
    
    private fun changeMode(screen: EmergencyScreen) {
        _state.value = _state.value.copy(emergencyScreen = screen)
    }
    
    private fun goBack() {
        _state.value = _state.value.copy(emergencyScreen = EmergencyScreen.COUPLED)
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
    
    private fun oneClickSafe() {
        if (state.value.emergencyButton == EmergencyButton.InDangerButton) {
            goPinCode()
        }
    }
    
    private fun doubleClickSos() {
        _state.value = _state.value.copy(emergencyScreen = EmergencyScreen.COUPLED)
        if (state.value.emergencyButton == EmergencyButton.InSafeButton) {
            _state.value = _state.value.copy(emergencyButton = EmergencyButton.InDangerButton)
        }
    }
    
    fun disableSosStatus() {
        _state.value = _state.value.copy(emergencyScreen = EmergencyScreen.NORMAL)
        _state.value = _state.value.copy(emergencyButton = EmergencyButton.InSafeButton)
	}
}


    



