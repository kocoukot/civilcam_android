package com.civilcam.ui.profile.userProfile.model

import com.civilcam.ext_features.compose.ComposeFragmentRoute

sealed class UserProfileRoute : ComposeFragmentRoute {
    object GoBack : UserProfileRoute()
    object GoGalleryOpen : UserProfileRoute()
    object GoPinCode : UserProfileRoute()
    data class GoCredentials(val userProfileType: UserProfileType, val credential: String?) :
        UserProfileRoute()

    object ForceLogout : UserProfileRoute()

}