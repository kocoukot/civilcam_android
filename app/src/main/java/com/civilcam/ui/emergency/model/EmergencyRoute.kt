package com.civilcam.ui.emergency.model

import com.civilcam.ext_features.compose.ComposeFragmentRoute

sealed class EmergencyRoute : ComposeFragmentRoute {
    object GoUserProfile : EmergencyRoute()
    object GoSettings : EmergencyRoute()
    object GoPinCode : EmergencyRoute()
    data class CheckPermission(val isSos: Boolean) : EmergencyRoute()
    data class ControlFlash(val enabled: Boolean, val cameraState: Int) : EmergencyRoute()
    object ShowSystemUI : EmergencyRoute()
    object HideSystemUI : EmergencyRoute()
    data class IsNavBarVisible(val isVisible: Boolean) : EmergencyRoute()

}