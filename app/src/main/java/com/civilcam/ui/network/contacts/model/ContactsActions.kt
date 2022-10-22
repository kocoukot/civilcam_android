package com.civilcam.ui.network.contacts.model

import com.civilcam.ext_features.compose.ComposeFragmentActions

sealed class ContactsActions : ComposeFragmentActions {
    object ClickGoBack : ContactsActions()
    object ClickGoInviteByNumber : ContactsActions()
    object ClearErrorText : ContactsActions()

    data class ClickInvite(val contact: PersonContactItem) : ContactsActions()
    data class ClickSearch(val searchString: String) : ContactsActions()
}