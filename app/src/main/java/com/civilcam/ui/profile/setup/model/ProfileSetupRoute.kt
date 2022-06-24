package com.civilcam.ui.profile.setup.model

import com.civilcam.common.ext.compose.ComposeFragmentRoute

sealed class ProfileSetupRoute : ComposeFragmentRoute {
    object GoSubscription : ProfileSetupRoute()
    object GoBack : ProfileSetupRoute()
    object GoLocationSelect : ProfileSetupRoute()
    object GoGalleryOpen : ProfileSetupRoute()

}
