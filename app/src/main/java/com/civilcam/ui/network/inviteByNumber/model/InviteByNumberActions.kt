package com.civilcam.ui.network.inviteByNumber.model

import com.civilcam.ext_features.compose.ComposeFragmentActions

sealed class InviteByNumberActions : ComposeFragmentActions {
    object ClickGoBack : InviteByNumberActions()
    object PhoneCleared : InviteByNumberActions()
    object ClickCloseScreenAlert : InviteByNumberActions()
    data class PhoneEntered(val phoneNumber: String) : InviteByNumberActions()

    object SendInvite : InviteByNumberActions()
}