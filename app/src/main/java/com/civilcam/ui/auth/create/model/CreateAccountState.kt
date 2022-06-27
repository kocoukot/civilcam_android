package com.civilcam.ui.auth.create.model

import com.civilcam.common.ext.compose.ComposeFragmentState
import com.civilcam.common.ext.isEmail

data class CreateAccountState(
	val isLoading: Boolean = false,
	val errorText: String = "Invalid email. Please try again. (eg:email@gmail.com)",
	val email: String = "",
	val password: String = "",
	val confirmPassword: String = "",
	val isEmail: Boolean = false
) : ComposeFragmentState {
	
	val isFilled: Boolean
		get() = email.isEmail() &&
				password == confirmPassword
	
	val noMatch: Boolean
		get() = (password.isNotEmpty() && confirmPassword.isNotEmpty()) && (password != confirmPassword)
}