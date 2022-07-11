package com.civilcam.ui.emergency

import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.ui.emergency.model.EmergencyActions
import com.civilcam.ui.emergency.model.EmergencyButton
import com.civilcam.ui.emergency.model.EmergencyRoute
import com.civilcam.ui.emergency.model.EmergencyState
import kotlinx.coroutines.flow.MutableStateFlow


class EmergencyViewModel : ComposeViewModel<EmergencyState, EmergencyRoute, EmergencyActions>() {
    override var _state: MutableStateFlow<EmergencyState> = MutableStateFlow(EmergencyState())

    override fun setInputActions(action: EmergencyActions) {
        when (action) {
            EmergencyActions.DoubleClickSos -> doubleClickSos()
        }
    }

    private fun doubleClickSos() {
        _state.value = _state.value.copy(emergencyButton = EmergencyButton.InDangerButton)
    }
}


    



