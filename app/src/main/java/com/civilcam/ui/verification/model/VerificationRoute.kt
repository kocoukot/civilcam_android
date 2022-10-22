package com.civilcam.ui.verification.model

import com.civilcam.ext_features.compose.ComposeFragmentRoute

sealed class VerificationRoute : ComposeFragmentRoute {
	object GoBack : VerificationRoute()
	object ToNextScreen : VerificationRoute()
	data class GoPasswordCreate(val token: String) : VerificationRoute()
}