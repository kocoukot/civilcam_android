package com.civilcam.ui.profile.userProfile.model

import com.civilcam.common.ext.compose.ComposeFragmentActions
import com.civilcam.ui.profile.setup.model.UserInfoDataType

sealed class UserProfileActions : ComposeFragmentActions {
	object GoBack : UserProfileActions()
	object ClickAvatarSelect : UserProfileActions()
	object ClickDateSelect : UserProfileActions()
	object ClickCloseDatePicker : UserProfileActions()
	data class ClickSelectDate(val date: Long) : UserProfileActions()
	data class GoEdit(val screenType: UserProfileScreen) : UserProfileActions()
	data class GoCredentials(val userProfileType: UserProfileType) : UserProfileActions()
	data class GoSave(val screenType: UserProfileScreen) : UserProfileActions()
	data class EnterInputData(val dataType: UserInfoDataType, val data: String) :
		UserProfileActions()
}