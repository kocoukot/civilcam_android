package com.civilcam.ui.profile.userProfile.model

import com.civilcam.common.ext.compose.ComposeFragmentActions
import com.civilcam.domain.model.AutocompletePlace
import com.civilcam.ui.profile.setup.model.ProfileSetupActions
import com.civilcam.ui.profile.setup.model.UserInfoDataType

sealed class UserProfileActions : ComposeFragmentActions {
	object GoBack : UserProfileActions()
	object ClickAvatarSelect : UserProfileActions()
	object ClickLocationSelect : UserProfileActions()
	object ClickDateSelect : UserProfileActions()
	object ClickCloseDatePicker : UserProfileActions()
	data class ClickSelectDate(val date: Long) : UserProfileActions()
	object ClickEdit : UserProfileActions()
	data class GoCredentials(val userProfileType: UserProfileType) : UserProfileActions()
	object ClickSave : UserProfileActions()
	data class EnterInputData(val dataType: UserInfoDataType, val data: String) :
		UserProfileActions()
	data class LocationSearchQuery(val searchQuery: String) : UserProfileActions()
	data class ClickAddressSelect(val address: AutocompletePlace) : UserProfileActions()
}