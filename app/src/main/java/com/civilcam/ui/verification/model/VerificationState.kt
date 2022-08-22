package com.civilcam.ui.verification.model

import com.civilcam.common.ext.compose.ComposeFragmentState
import com.civilcam.domainLayer.model.VerificationFlow

data class VerificationState(
    val isLoading: Boolean = false,
    var errorText: String = "",
    val hasError: Boolean = false,
    val timeOut: String = "",
    val verificationFlow: VerificationFlow = VerificationFlow.CURRENT_EMAIL,
    val verificationSubject: String = "",
    val newSubject: String = "",
    val token: String = ""
) : ComposeFragmentState