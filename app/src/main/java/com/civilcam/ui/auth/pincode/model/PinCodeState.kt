package com.civilcam.ui.auth.pincode.model

import com.civilcam.common.ext.compose.ComposeFragmentState

data class PinCodeState(
	val isLoading: Boolean = false,
	val errorText: String = "Invalid email. Please try again. (eg:email@gmail.com)",
	
	val pinCode: String = "",
	val confirmPinCode: String = "",
	val currentPinCode: String = "",
	
	val noMatch: Boolean = false,
	val currentNoMatch: Boolean = false,
	val newPinNoMatch: Boolean = false,
	
	val screenState: PinCodeFlow = PinCodeFlow.CREATE_PIN_CODE,
) : ComposeFragmentState {
	
	val isMatch = pinCode == confirmPinCode
	val isCurrentPin = currentPinCode == "1111"
	val sosMatch = pinCode == "1111"
	
}