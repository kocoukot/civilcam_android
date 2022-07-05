package com.civilcam.ui.profile.userProfile.model

import com.civilcam.common.ext.compose.ComposeFragmentActions

sealed class UserProfileActions : ComposeFragmentActions {
	object GoBack : UserProfileActions()
	data class GoEdit(val screenType: UserProfileScreen) : UserProfileActions()
	data class GoCredentials(val userProfileType: UserProfileType) : UserProfileActions()
	data class GoSave(val screenType: UserProfileScreen) : UserProfileActions()
}