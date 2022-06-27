package com.civilcam.ui.auth.login.model

import com.civilcam.common.ext.compose.ComposeFragmentRoute

sealed class LoginRoute : ComposeFragmentRoute {
	object GoBack : LoginRoute()
}
