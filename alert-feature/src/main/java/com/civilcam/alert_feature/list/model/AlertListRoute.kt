package com.civilcam.alert_feature.list.model

import com.civilcam.alert_feature.R
import com.civilcam.alert_feature.map.LiveMapFragment
import com.civilcam.ext_features.arch.ComposeRouteNavigation
import com.civilcam.ext_features.compose.ComposeFragmentRoute

sealed class AlertListRoute : ComposeFragmentRoute {
    object GoMyProfile : AlertListRoute() {
        override fun handleRoute(): ComposeRouteNavigation =
            ComposeRouteNavigation.DeepLinkNavigate(destination = com.civilcam.ext_features.R.string.direction_userProfileFragment)
    }

    object GoSettings : AlertListRoute() {
        override fun handleRoute(): ComposeRouteNavigation =
            ComposeRouteNavigation.DeepLinkNavigate(destination = com.civilcam.ext_features.R.string.direction_settingsFragment)
    }

    object GoAlertHistory : AlertListRoute() {
        override fun handleRoute(): ComposeRouteNavigation =
            ComposeRouteNavigation.GraphNavigate(destination = R.id.action_alerts_root_to_alertsHistoryFragment)
    }

    data class GoUserAlert(private val alertId: Int) : AlertListRoute() {
        override fun handleRoute(): ComposeRouteNavigation = ComposeRouteNavigation.GraphNavigate(
            destination = R.id.liveMapFragment, LiveMapFragment.createArgs(alertId)
        )
    }
}
