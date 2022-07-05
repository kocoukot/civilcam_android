package com.civilcam.ui.profile.userProfile.model

import com.civilcam.common.ext.compose.ComposeFragmentState

data class UserProfileState(
	val isLoading: Boolean = false,
	val errorText: String = "",
	val userId: Int = 0,
	val screenState: UserProfileScreen = UserProfileScreen.PROFILE
) : ComposeFragmentState