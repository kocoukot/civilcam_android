package com.civilcam.ui.terms.model

import com.civilcam.domainLayer.model.docs.LegalDocs
import com.civilcam.ext_features.compose.ComposeFragmentState

data class TermsState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val isSettings: Boolean = true,
    val isTermsAccepted: Boolean = false,
    val legalDocs: LegalDocs? = null
) : ComposeFragmentState