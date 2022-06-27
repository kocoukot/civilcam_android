package com.civilcam.ui.auth.password.model

import com.civilcam.common.ext.compose.ComposeFragmentState
import com.civilcam.common.ext.isEmail

data class ResetState(
	val errorText: String = "Invalid email. Please try again. (eg:email@gmail.com)",
	val email: String = "",
	val isEmail: Boolean = false
) : ComposeFragmentState {
	val isFilled: Boolean = email.isNotEmpty() && email.isEmail()
}