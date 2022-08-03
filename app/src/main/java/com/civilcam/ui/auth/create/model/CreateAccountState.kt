package com.civilcam.ui.auth.create.model

import com.civilcam.common.ext.compose.ComposeFragmentState
import com.civilcam.common.ext.isEmail

data class CreateAccountState(
	val isLoading: Boolean = false,
	val alertErrorText: String = "",
	val emailErrorText: String = "",
	val email: String = "",
	val passwordModel: PasswordModel = PasswordModel(),
	val isEmail: Boolean = true
) : ComposeFragmentState {
	
	val isFilled: Boolean
		get() = email.isEmail() &&
				passwordModel.isFilled
}