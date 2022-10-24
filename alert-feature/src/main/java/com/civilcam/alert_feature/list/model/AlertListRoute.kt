package com.civilcam.alert_feature.list.model

import android.os.Bundle
import com.civilcam.alert_feature.R
import com.civilcam.alert_feature.map.LiveMapFragment
import com.civilcam.ext_features.arch.ComposeRouteNavigation
import com.civilcam.ext_features.compose.ComposeFragmentRoute

sealed class AlertListRoute : ComposeFragmentRoute {
    object GoMyProfile : AlertListRoute(), ComposeRouteNavigation.DeepLinkNavigate {
        override val destination: Int =
            com.civilcam.ext_features.R.string.direction_userProfileFragment
    }

    object GoSettings : AlertListRoute(), ComposeRouteNavigation.DeepLinkNavigate {
        override val destination: Int =
            com.civilcam.ext_features.R.string.direction_settingsFragment
    }

    object GoAlertHistory : AlertListRoute(), ComposeRouteNavigation.GraphNavigate {
        override val destination: Int = R.id.action_alerts_root_to_alertsHistoryFragment
    }

    data class GoUserAlert(val alertId: Int) : AlertListRoute(),
        ComposeRouteNavigation.GraphNavigate {
        override val destination: Int = R.id.liveMapFragment
        override val bundle: Bundle = LiveMapFragment.createArgs(alertId)
    }
}
