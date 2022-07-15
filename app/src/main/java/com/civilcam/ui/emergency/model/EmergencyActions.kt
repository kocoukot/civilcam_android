package com.civilcam.ui.emergency.model

import com.civilcam.common.ext.compose.ComposeFragmentActions

sealed class EmergencyActions : ComposeFragmentActions {
    object DoubleClickSos : EmergencyActions()
    object OneClickSafe : EmergencyActions()
    object GoUserProfile : EmergencyActions()
    object GoSettings : EmergencyActions()
    object GoBack : EmergencyActions()
    object DetectLocation : EmergencyActions()
    object ControlFlash : EmergencyActions()
    object ChangeCamera : EmergencyActions()
    object MinimizeMap : EmergencyActions()
    object MaximizeMap : EmergencyActions()
    object MinimizeLive : EmergencyActions()
    object MaximizeLive : EmergencyActions()
    data class ChangeMode(val mode: EmergencyScreen) : EmergencyActions()
}
