package com.civilcam.ui.terms.model

import com.civilcam.common.ext.compose.ComposeFragmentActions

sealed class TermsActions : ComposeFragmentActions {
    object ClickGoBack : TermsActions()
    object ClickContinue : TermsActions()
    data class ClickDocument(val webLink: com.civilcam.domainLayer.model.TermsType) : TermsActions()
}
