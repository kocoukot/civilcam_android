package com.civilcam.ui.auth.password.create.model

import com.civilcam.common.ext.compose.ComposeFragmentState

data class CreatePasswordState(
	val isLoading: Boolean = false,
	val errorText: String = "Invalid email. Please try again. (eg:email@gmail.com)",
	val password: String = "",
	val confirmPassword: String = "",
	val token: String = ""
) : ComposeFragmentState {
	val isFilled: Boolean
		get() = password == confirmPassword &&
				password.isNotEmpty() && confirmPassword.isNotEmpty()
	
	val noMatch: Boolean
		get() = (password.isNotEmpty() && confirmPassword.isNotEmpty()) && (password != confirmPassword)
}