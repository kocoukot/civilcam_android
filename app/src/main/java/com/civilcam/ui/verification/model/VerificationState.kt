package com.civilcam.ui.verification.model

import com.civilcam.common.ext.compose.ComposeFragmentState

data class VerificationState(
    val isLoading: Boolean = false,
    var errorText: String = "",
    val hasError: Boolean = false,
    val timeOut: String = "",
    val verificationFlow: com.civilcam.domainLayer.model.VerificationFlow = com.civilcam.domainLayer.model.VerificationFlow.NEW_EMAIL,
    val verificationSubject: String = ""
) : ComposeFragmentState