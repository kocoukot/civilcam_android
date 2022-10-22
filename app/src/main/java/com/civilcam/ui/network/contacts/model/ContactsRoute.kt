package com.civilcam.ui.network.contacts.model

import com.civilcam.ext_features.compose.ComposeFragmentRoute

sealed class ContactsRoute : ComposeFragmentRoute {
    object GoBack : ContactsRoute()
    object GoInviteByNumber : ContactsRoute()
}