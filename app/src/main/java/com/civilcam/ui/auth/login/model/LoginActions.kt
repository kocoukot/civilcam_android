package com.civilcam.ui.auth.login.model

import com.civilcam.common.ext.compose.ComposeFragmentActions

sealed class LoginActions : ComposeFragmentActions{
	object ClickGoBack : LoginActions()
}