package com.civilcam.ui.profile.credentials.model

import com.civilcam.ext_features.compose.ComposeFragmentState
import com.civilcam.ext_features.isEmail
import com.civilcam.ui.profile.userProfile.model.UserProfileType

data class ChangeCredentialsState(
	val isLoading: Boolean = false,
	val errorText: String = "",
	val email: String = "",
	val phone: String = "",
	val isEmail: Boolean = true,
	val screenState: UserProfileType = UserProfileType.PHONE_NUMBER,
	val currentEmail: String = "",
	val emailError: Boolean = false,
	val phoneError: Boolean = false
) : ComposeFragmentState {
	
	val validPhone = phone.length == 10
	val isFilled: Boolean = email.isNotEmpty() && email.isEmail()
}