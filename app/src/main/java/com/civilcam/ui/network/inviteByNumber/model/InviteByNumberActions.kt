package com.civilcam.ui.network.inviteByNumber.model

import com.civilcam.common.ext.compose.ComposeFragmentActions

sealed class InviteByNumberActions : ComposeFragmentActions {
    object ClickGoBack : InviteByNumberActions()
    object PhoneCleared : InviteByNumberActions()
    data class SendInvite(val phoneNumber: String) : InviteByNumberActions()

}
