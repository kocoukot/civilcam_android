package com.civilcam.ui.auth.password.reset.model

import com.civilcam.ext_features.compose.ComposeFragmentRoute

sealed class ResetRoute : ComposeFragmentRoute {
	data class GoContinue(val email: String) : ResetRoute()
	object GoBack : ResetRoute()
}