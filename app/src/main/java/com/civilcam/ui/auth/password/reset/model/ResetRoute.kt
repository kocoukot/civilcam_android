package com.civilcam.ui.auth.password.reset.model

import com.civilcam.common.ext.compose.ComposeFragmentRoute

sealed class ResetRoute : ComposeFragmentRoute {
	data class GoContinue(val email: String) : ResetRoute()
	object GoBack : ResetRoute()
}