package com.civilcam.ui.auth.password.reset.model

import com.civilcam.ext_features.compose.ComposeFragmentState
import com.civilcam.ext_features.isEmail

data class ResetState(
	val errorText: String = "Invalid email. Please try again. (eg:email@gmail.com)",
	val isLoading: Boolean = false,
	val email: String = "",
	val isEmail: Boolean = false,
	var onTouchMode: Boolean = false,
	var emailError: Boolean = false
) : ComposeFragmentState {
	val isFilled: Boolean = email.isNotEmpty() && email.isEmail()
}