package com.civilcam.ui.emergency.model

import com.civilcam.ext_features.compose.ComposeFragmentRoute

sealed class EmergencyRoute : ComposeFragmentRoute {
    object GoUserProfile : EmergencyRoute()
    object GoSettings : EmergencyRoute()
    object GoPinCode : EmergencyRoute()
    object ChangeCamera : EmergencyRoute()
    object ShowSystemUI : EmergencyRoute()
    object HideSystemUI : EmergencyRoute()
    object StopStream : EmergencyRoute()
    data class CheckPermission(val isSos: Boolean) : EmergencyRoute()
    data class GoLive(val streamKey: String) : EmergencyRoute()
    data class IsNavBarVisible(val isVisible: Boolean) : EmergencyRoute()

}