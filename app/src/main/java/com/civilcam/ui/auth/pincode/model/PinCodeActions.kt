package com.civilcam.ui.auth.pincode.model

import com.civilcam.common.ext.compose.ComposeFragmentActions

sealed class PinCodeActions : ComposeFragmentActions {
	object GoBack : PinCodeActions()
	object GoGuardians : PinCodeActions()
	object GoConfirm : PinCodeActions()
	data class EnterPinCode(val pinCode: String, val inputType: PinCodeInputDataType) : PinCodeActions()
}