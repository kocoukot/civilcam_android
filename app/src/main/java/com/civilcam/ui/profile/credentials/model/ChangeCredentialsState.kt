package com.civilcam.ui.profile.credentials.model

import com.civilcam.common.ext.compose.ComposeFragmentState
import com.civilcam.common.ext.isEmail

data class ChangeCredentialsState(
	val isLoading: Boolean = false,
	val errorText: String = "Invalid email. Please try again. (eg:email@gmail.com)",
	val phoneError: String = "This phone number is already taken. Please try another one",
	val email: String = "",
	val phone: String = "",
	val isEmail: Boolean = true,
	val screenState: CredentialType = CredentialType.PHONE
) : ComposeFragmentState {
	
	//for ui test
	val phoneTaken = phone == "1111111111"
	
	val validPhone = phone != "1111111111" && phone.length == 10
	
	val isFilled: Boolean = email.isNotEmpty() && email.isEmail()
}