package com.civilcam.ui.auth.pincode.model

enum class PinCodeFlow(val rawValue: String) {
	CREATE_PIN_CODE("CREATE_PIN_CODE"),
	CONFIRM_PIN_CODE("CONFIRM_NEW_PIN_CODE"),
	CURRENT_PIN_CODE("CURRENT_PIN_CODE"),
	NEW_PIN_CODE("NEW_PIN_CODE"),
	CONFIRM_NEW_PIN_CODE("CONFIRM_NEW_PIN_CODE"),
}