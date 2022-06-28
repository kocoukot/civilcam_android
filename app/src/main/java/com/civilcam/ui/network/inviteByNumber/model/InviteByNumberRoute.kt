package com.civilcam.ui.network.inviteByNumber.model

import com.civilcam.common.ext.compose.ComposeFragmentRoute

sealed class InviteByNumberRoute : ComposeFragmentRoute {
    object GoBack : InviteByNumberRoute()

}