package com.civilcam.ui.profile.userDetails.model

import com.civilcam.ext_features.compose.ComposeFragmentRoute

sealed class UserDetailsRoute : ComposeFragmentRoute {
    object GoBack : UserDetailsRoute()
}
