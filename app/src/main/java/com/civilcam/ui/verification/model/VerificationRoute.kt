package com.civilcam.ui.verification.model

import com.civilcam.common.ext.compose.ComposeFragmentRoute

sealed class VerificationRoute : ComposeFragmentRoute {
	object GoBack : VerificationRoute()
	object ToNextScreen : VerificationRoute()
}