package com.civilcam.ui.auth.login.model

import com.civilcam.common.ext.compose.ComposeFragmentState

data class LoginState(
	val isLoading: Boolean = false,
	val errorText: String = "",
) : ComposeFragmentState