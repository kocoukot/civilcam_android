package com.civilcam.ui.profile.userProfile.model

import com.civilcam.common.ext.compose.ComposeFragmentState
import com.civilcam.ui.profile.userDetails.model.UserDetailsModel

data class UserProfileState(
	val isLoading: Boolean = false,
	val errorText: String = "",
	val data: UserDetailsModel? = null,
	val screenState: UserProfileScreen = UserProfileScreen.PROFILE
) : ComposeFragmentState