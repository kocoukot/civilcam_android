package com.civilcam.ui.verification.model

import com.civilcam.common.ext.compose.ComposeFragmentState

data class VerificationState(
	val isLoading: Boolean = false,
	val errorText: String = "",
	val hasError: Boolean = false,
	val timeOut: String = ""
) : ComposeFragmentState