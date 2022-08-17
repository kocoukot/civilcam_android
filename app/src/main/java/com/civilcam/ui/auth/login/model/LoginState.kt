package com.civilcam.ui.auth.login.model

import com.civilcam.common.ext.compose.ComposeFragmentState
import com.civilcam.common.ext.isEmail

data class LoginState(
	val isLoading: Boolean = false,
	val errorText: String = "Invalid email. Please try again. (eg:email@gmail.com)",
	val email: String = "",
	val password: String = "",
	val isEmail: Boolean = false,
	val emailError: Boolean = false,
	val credError: Boolean = false
) : ComposeFragmentState {
	val isFilled: Boolean = email.isEmail() && email.isNotEmpty() && password.isNotEmpty()
}