package com.civilcam.ui.profile.credentials.model

import com.civilcam.ext_features.compose.ComposeFragmentRoute
import com.civilcam.ui.profile.userProfile.model.UserProfileType

sealed class ChangeCredentialsRoute : ComposeFragmentRoute {
    object GoBack : ChangeCredentialsRoute()
    data class GoSave(
        val dataType: UserProfileType,
        val data: String,
        val currentEmail: String = ""
    ) : ChangeCredentialsRoute()

    object ForceLogout : ChangeCredentialsRoute()

}