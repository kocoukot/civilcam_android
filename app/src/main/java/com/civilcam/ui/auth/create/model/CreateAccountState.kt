package com.civilcam.ui.auth.create.model

import com.civilcam.common.ext.compose.ComposeFragmentState
import com.civilcam.common.ext.isEmail

data class CreateAccountState(
	val isLoading: Boolean = false,
	val errorText: String = "Invalid email. Please try again. (eg:email@gmail.com)",
	val email: String = "",
	val passwordModel: PasswordModel = PasswordModel(),
	val isEmail: Boolean = true
) : ComposeFragmentState {
	
	val isFilled: Boolean
		get() = email.isEmail() &&
				passwordModel.isFilled
}