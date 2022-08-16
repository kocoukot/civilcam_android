package com.civilcam.ui.profile.credentials.model

import com.civilcam.common.ext.compose.ComposeFragmentState
import com.civilcam.common.ext.isEmail

data class ChangeCredentialsState(
	val isLoading: Boolean = false,
	val errorText: String = "",
	val email: String = "",
	val phone: String = "",
	val isEmail: Boolean = true,
	val screenState: CredentialType = CredentialType.PHONE,
	val currentEmail: String = "",
	val emailError: Boolean = false,
	val phoneError: Boolean = false
) : ComposeFragmentState {
	
	val validPhone = phone.length == 10
	val isFilled: Boolean = email.isNotEmpty() && email.isEmail()
}