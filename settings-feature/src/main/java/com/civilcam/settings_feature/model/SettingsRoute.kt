package com.civilcam.settings_feature.model

import com.civilcam.ext_features.arch.ComposeRouteNavigation
import com.civilcam.ext_features.compose.ComposeFragmentRoute


sealed class SettingsRoute : ComposeFragmentRoute {
    object GoBack : SettingsRoute(), ComposeRouteNavigation.PopNavigation
    object GoTerms : SettingsRoute(), ComposeRouteNavigation.DeepLinkNavigate {
        override val destination: Int = com.civilcam.ext_features.R.string.direction_termsFragment
        override val arguments = "/${true}"
    }

    object GoSubManage : SettingsRoute(), ComposeRouteNavigation.DeepLinkNavigate {
        override val destination: Int =
            com.civilcam.ext_features.R.string.direction_subscriptionFragment
        override val arguments = "/${true}"
    }

    object ForceLogout : SettingsRoute(), ComposeRouteNavigation.NavigateToStart
    object GoStartScreen : SettingsRoute(), ComposeRouteNavigation.NavigateToStart
}