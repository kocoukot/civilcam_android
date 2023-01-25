package com.civilcam.ui.network.contacts.model

import com.civilcam.R
import com.civilcam.ext_features.arch.ComposeRouteNavigation
import com.civilcam.ext_features.compose.ComposeFragmentRoute

sealed class ContactsRoute : ComposeFragmentRoute {
    object GoBack : ContactsRoute() {
        override fun handleRoute(): ComposeRouteNavigation = ComposeRouteNavigation.PopNavigation
    }

    object GoInviteByNumber : ContactsRoute() {
        override fun handleRoute(): ComposeRouteNavigation =
            ComposeRouteNavigation.GraphNavigate(R.id.action_contactsFragment_to_inviteByNumberFragment)

    }
}