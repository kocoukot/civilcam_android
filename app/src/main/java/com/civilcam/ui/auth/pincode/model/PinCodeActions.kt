package com.civilcam.ui.auth.pincode.model

import com.civilcam.ext_features.compose.ComposeFragmentActions

sealed class PinCodeActions : ComposeFragmentActions {
	object GoBack : PinCodeActions()
	data class EnterPinCode(val pinCode: String) : PinCodeActions()
}