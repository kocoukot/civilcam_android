package com.civilcam.ui.auth.pincode.model

import com.civilcam.common.ext.compose.ComposeFragmentState

data class PinCodeState(
	val isLoading: Boolean = false,
	val errorText: String = "Invalid email. Please try again. (eg:email@gmail.com)",
	val pinCode: String = "",
	val isConfirm: Boolean = false
) : ComposeFragmentState