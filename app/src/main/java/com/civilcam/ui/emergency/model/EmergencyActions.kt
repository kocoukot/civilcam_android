package com.civilcam.ui.emergency.model

import com.civilcam.common.ext.compose.ComposeFragmentActions

sealed class EmergencyActions : ComposeFragmentActions {
    object DoubleClickSos : EmergencyActions()
    object OneClickSafe : EmergencyActions()
    object GoUserProfile : EmergencyActions()
    object GoSettings : EmergencyActions()
    object GoBack : EmergencyActions()
}
