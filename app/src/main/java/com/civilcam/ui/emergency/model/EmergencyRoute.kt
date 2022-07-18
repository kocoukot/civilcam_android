package com.civilcam.ui.emergency.model

import com.civilcam.common.ext.compose.ComposeFragmentRoute

sealed class EmergencyRoute : ComposeFragmentRoute {
    object GoUserProfile : EmergencyRoute()
    object GoSettings : EmergencyRoute()
    object GoPinCode : EmergencyRoute()
    object CheckPermission : EmergencyRoute()
    data class ScreenState(val screen: EmergencyScreen) : EmergencyRoute()
    data class ControlFlash(val enabled: Boolean, val cameraState: Int) : EmergencyRoute()


    object ShowSystemUI : EmergencyRoute()
    object HideSystemUI : EmergencyRoute()
    data class IsNavBarVisible(val isVisible: Boolean) : EmergencyRoute()

}