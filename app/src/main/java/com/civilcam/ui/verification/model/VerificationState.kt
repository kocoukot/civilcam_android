package com.civilcam.ui.verification.model

import com.civilcam.domainLayer.model.VerificationFlow
import com.civilcam.ext_features.compose.ComposeFragmentState

data class VerificationState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val hasError: Boolean = false,
    val timeOut: String = "",
    val verificationFlow: VerificationFlow = VerificationFlow.CURRENT_EMAIL,
    val verificationSubject: String = "",
    val newSubject: String = "",
    val token: String = ""
) : ComposeFragmentState