package com.civilcam.ui.verification.model

import com.civilcam.common.ext.compose.ComposeFragmentActions

sealed class VerificationActions : ComposeFragmentActions {
	object ClickGoBack : VerificationActions()
}