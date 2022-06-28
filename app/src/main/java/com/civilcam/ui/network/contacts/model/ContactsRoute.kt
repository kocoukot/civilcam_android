package com.civilcam.ui.network.contacts.model

import com.civilcam.common.ext.compose.ComposeFragmentRoute

sealed class ContactsRoute : ComposeFragmentRoute {
    object GoBack : ContactsRoute()
    object GoInviteByNumber : ContactsRoute()

}