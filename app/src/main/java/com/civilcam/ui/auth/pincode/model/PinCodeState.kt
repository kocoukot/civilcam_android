package com.civilcam.ui.auth.pincode.model

import com.civilcam.ext_features.compose.ComposeFragmentState

data class PinCodeState(
	val isLoading: Boolean = false,
	val errorText: String = "",

	val pinCode: String = "",
	val confirmPinCode: String = "",
	val currentPinCode: String = "",

	val isDataLoaded: Boolean = false,

	val noMatch: Boolean = false,
	val currentNoMatch: Boolean = false,
	
	val screenState: PinCodeFlow = PinCodeFlow.CREATE_PIN_CODE,
) : ComposeFragmentState {
	
	val isMatch = pinCode == confirmPinCode
}