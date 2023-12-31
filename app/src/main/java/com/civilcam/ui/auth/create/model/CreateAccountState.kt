package com.civilcam.ui.auth.create.model

import com.civilcam.domainLayer.model.profile.PasswordModel
import com.civilcam.ext_features.compose.ComposeFragmentState
import com.civilcam.ext_features.ext.isEmail

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