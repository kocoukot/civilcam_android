package com.civilcam.alert_feature.history.model

import com.civilcam.ext_features.arch.ComposeRouteCallNumber
import com.civilcam.ext_features.arch.ComposeRouteNavigation
import com.civilcam.ext_features.compose.ComposeFragmentRoute

sealed class AlertHistoryRoute : ComposeFragmentRoute {
    object GoBack : AlertHistoryRoute(), ComposeRouteNavigation.PopNavigation
    object ForceLogout : AlertHistoryRoute(), ComposeRouteNavigation.NavigateToStart
    class CallUser(userNumber: String) : AlertHistoryRoute(), ComposeRouteCallNumber {
        override val phoneNumber: String = userNumber
    }
}
