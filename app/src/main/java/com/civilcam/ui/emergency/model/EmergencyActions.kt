package com.civilcam.ui.emergency.model

import com.civilcam.common.ext.compose.ComposeFragmentActions

sealed class EmergencyActions : ComposeFragmentActions {
    object DoubleClickSos : EmergencyActions()
}
