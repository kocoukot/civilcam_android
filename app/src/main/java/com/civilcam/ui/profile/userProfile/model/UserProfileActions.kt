package com.civilcam.ui.profile.userProfile.model

import com.civilcam.common.ext.compose.ComposeFragmentActions

sealed class UserProfileActions : ComposeFragmentActions {
	object GoBack : UserProfileActions()
	object GoEdit : UserProfileActions()
	object GoPhoneNumber : UserProfileActions()
	object GoEmail : UserProfileActions()
	object GoPinCode : UserProfileActions()
}