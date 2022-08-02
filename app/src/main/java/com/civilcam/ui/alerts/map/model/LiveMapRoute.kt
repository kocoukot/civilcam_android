package com.civilcam.ui.alerts.map.model

import com.civilcam.common.ext.compose.ComposeFragmentRoute

sealed class LiveMapRoute : ComposeFragmentRoute {

    object GoBack : LiveMapRoute()
    data class CallUserPhone(val userPhoneNumber: String) : LiveMapRoute()
    object CallPolice : LiveMapRoute()

}