package com.civilcam.ui.network.main.model

import com.civilcam.R
import com.civilcam.ext_features.arch.ComposeRouteNavigation
import com.civilcam.ext_features.compose.ComposeFragmentRoute
import com.civilcam.ui.profile.userDetails.UserDetailsFragment

sealed class NetworkMainRoute : ComposeFragmentRoute {
    object GoSettings : NetworkMainRoute() {
        override fun handleRoute(): ComposeRouteNavigation =
            ComposeRouteNavigation.GraphNavigate(destination = R.id.action_network_root_to_settingsFragment)
    }

    object GoContacts : NetworkMainRoute()
    object GoProfile : NetworkMainRoute() {
        override fun handleRoute(): ComposeRouteNavigation =
            ComposeRouteNavigation.GraphNavigate(destination = R.id.userProfileFragment)
    }

    data class GoUserDetail(private val userId: Int) : NetworkMainRoute() {
        override fun handleRoute(): ComposeRouteNavigation =
            ComposeRouteNavigation.GraphNavigate(
                destination = R.id.action_network_root_to_userDetailsFragment,
                UserDetailsFragment.createArgs(userId)
            )
    }

    data class IsNavBarVisible(val isVisible: Boolean) : NetworkMainRoute()


    object ForceLogout : NetworkMainRoute() {
        override fun handleRoute(): ComposeRouteNavigation = ComposeRouteNavigation.NavigateToStart
    }

}