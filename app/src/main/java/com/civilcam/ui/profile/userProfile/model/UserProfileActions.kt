package com.civilcam.ui.profile.userProfile.model

import com.civilcam.domainLayer.model.AutocompletePlace
import com.civilcam.ext_features.compose.ComposeFragmentActions
import com.civilcam.ui.profile.setup.model.UserInfoDataType

sealed class UserProfileActions : ComposeFragmentActions {
	object GoBack : UserProfileActions()
	object ClickAvatarSelect : UserProfileActions()
	object ClickLocationSelect : UserProfileActions()
	object ClickDateSelect : UserProfileActions()
	data class ClickSelectDate(val date: Long? = null) : UserProfileActions()
	object ClickEdit : UserProfileActions()
	data class GoCredentials(val userProfileType: UserProfileType) : UserProfileActions()
	object ClickSave : UserProfileActions()
	data class EnterInputData(val dataType: UserInfoDataType, val data: String) :
		UserProfileActions()

	data class LocationSearchQuery(val searchQuery: String) : UserProfileActions()
	data class ClickAddressSelect(val address: AutocompletePlace) : UserProfileActions()

	object ClickCloseAlert : UserProfileActions()
}