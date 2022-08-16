package com.civilcam.ui.terms.model

import com.civilcam.common.ext.compose.ComposeFragmentState
import com.civilcam.domainLayer.model.docs.LegalDocs

data class TermsState(
    val isLoading: Boolean = false,
    var errorText: String = "",
    val isSettings: Boolean = true,
    val isTermsAccepted: Boolean = false,
    val legalDocs: LegalDocs? = null
) : ComposeFragmentState