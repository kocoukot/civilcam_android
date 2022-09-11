package com.civilcam.ui.verification.model

import com.civilcam.ext_features.compose.ComposeFragmentActions

sealed class VerificationActions : ComposeFragmentActions {
	object ClickGoBack : VerificationActions()
	data class EnterCodeData(val data: String) : VerificationActions()
	object ResendClick : VerificationActions()
	object ClickCloseAlert : VerificationActions()

}