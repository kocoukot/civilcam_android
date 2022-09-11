package com.civilcam.ui.alerts.map.model

import com.civilcam.ext_features.compose.ComposeFragmentRoute

sealed class LiveMapRoute : ComposeFragmentRoute {

    object GoBack : LiveMapRoute()
    data class CallUserPhone(val userPhoneNumber: String) : LiveMapRoute()
    object CallPolice : LiveMapRoute()

    object AlertResolved : LiveMapRoute()
    object CheckPermission : LiveMapRoute()
}