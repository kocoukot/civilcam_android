package com.civilcam.ui.network.inviteByNumber.model

import com.civilcam.ext_features.compose.ComposeFragmentRoute

sealed class InviteByNumberRoute : ComposeFragmentRoute {
    object GoBack : InviteByNumberRoute()
}