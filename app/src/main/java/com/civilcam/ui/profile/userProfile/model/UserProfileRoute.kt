package com.civilcam.ui.profile.userProfile.model

import com.civilcam.common.ext.compose.ComposeFragmentRoute

sealed class UserProfileRoute : ComposeFragmentRoute {
	object GoBack : UserProfileRoute()
	data class GoCredentials(val userProfileType: UserProfileType) : UserProfileRoute()
}