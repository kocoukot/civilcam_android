package com.civilcam.ui.auth.login.model

import com.civilcam.ext_features.compose.ComposeFragmentState
import com.civilcam.ext_features.ext.isEmail

data class LoginState(
    val isLoading: Boolean = false,
    val alertError: String = "",
    val errorText: String = "",
    val email: String = "",
    val password: String = "",
    val isEmail: Boolean = false,
    val emailError: Boolean = false,
    val credError: Boolean = false
) : ComposeFragmentState {
	val isFilled: Boolean = email.isEmail() && email.isNotEmpty() && password.isNotEmpty()
}