package com.civilcam.ui.profile.userDetails.model

import com.civilcam.common.ext.compose.ComposeFragmentRoute

sealed class UserDetailsRoute : ComposeFragmentRoute {
    object GoBack : UserDetailsRoute()
}
