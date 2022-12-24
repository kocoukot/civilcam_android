package com.civilcam.alert_feature.history.model

import android.net.Uri
import com.civilcam.ext_features.arch.ComposeRouteNavigation
import com.civilcam.ext_features.compose.ComposeFragmentRoute

sealed class AlertHistoryRoute : ComposeFragmentRoute {
    object GoBack : AlertHistoryRoute() {
        override fun handleRoute(): ComposeRouteNavigation = ComposeRouteNavigation.PopNavigation
    }

    object ForceLogout : AlertHistoryRoute() {
        override fun handleRoute(): ComposeRouteNavigation =
            ComposeRouteNavigation.NavigateToStart
    }

    class CallUser(private val userNumber: String) : AlertHistoryRoute() {
        override fun handleRoute(): ComposeRouteNavigation =
            ComposeRouteNavigation.ComposeRouteCallNumber(userNumber)
    }

    class OpenVideo(val videoUri: Uri) : AlertHistoryRoute()
}
