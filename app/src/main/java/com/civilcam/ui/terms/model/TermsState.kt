package com.civilcam.ui.terms.model

import com.civilcam.common.ext.compose.ComposeFragmentState

data class TermsState(
    val isSettings: Boolean = false,
    val isTermsAccepted: Boolean = false
) : ComposeFragmentState