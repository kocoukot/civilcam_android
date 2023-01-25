package com.civilcam.ui.profile.userDetails.model

import com.civilcam.ext_features.arch.ComposeRouteNavigation
import com.civilcam.ext_features.compose.ComposeFragmentRoute

sealed class UserDetailsRoute : ComposeFragmentRoute {
    object GoBack : UserDetailsRoute() {
        override fun handleRoute(): ComposeRouteNavigation = ComposeRouteNavigation.PopNavigation
    }
}
