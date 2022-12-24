package com.civilcam.settings_feature.model

import com.civilcam.domainLayer.model.subscription.UserSubscriptionState
import com.civilcam.ext_features.arch.ComposeRouteNavigation
import com.civilcam.ext_features.compose.ComposeFragmentRoute


sealed class SettingsRoute : ComposeFragmentRoute {
    object GoBack : SettingsRoute() {
        override fun handleRoute(): ComposeRouteNavigation = ComposeRouteNavigation.PopNavigation
    }

    object GoTerms : SettingsRoute() {
        override fun handleRoute(): ComposeRouteNavigation =
            ComposeRouteNavigation.DeepLinkNavigate(
                destination = com.civilcam.ext_features.R.string.direction_termsFragment,
                arguments = "/${true}"
            )
    }

    object GoSubManage : SettingsRoute() {
        override fun handleRoute(): ComposeRouteNavigation =
            ComposeRouteNavigation.DeepLinkNavigate(
                destination = com.civilcam.ext_features.R.string.direction_subscriptionFragment,
                arguments = "/${UserSubscriptionState.SUB_RESELECT}"
            )
    }

    object ForceLogout : SettingsRoute() {
        override fun handleRoute(): ComposeRouteNavigation = ComposeRouteNavigation.NavigateToStart
    }

    object GoStartScreen : SettingsRoute() {
        override fun handleRoute(): ComposeRouteNavigation = ComposeRouteNavigation.NavigateToStart
    }
}