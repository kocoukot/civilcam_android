package com.civilcam.ui.profile.setup.model

import com.civilcam.ext_features.compose.ComposeFragmentRoute

sealed class ProfileSetupRoute : ComposeFragmentRoute {
    data class GoVerification(val phoneNumber: String) : ProfileSetupRoute()
    object GoBack : ProfileSetupRoute()
}