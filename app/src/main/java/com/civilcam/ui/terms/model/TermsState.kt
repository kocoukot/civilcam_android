package com.civilcam.ui.terms.model

import com.civilcam.common.ext.compose.ComposeFragmentState
import com.civilcam.domain.model.docs.LegalDocs

data class TermsState(
    val isLoading: Boolean = false,
    var errorText: String = "",
    val isSettings: Boolean = false,
    val isTermsAccepted: Boolean = false,
    val legalDocs: LegalDocs? = null
) : ComposeFragmentState