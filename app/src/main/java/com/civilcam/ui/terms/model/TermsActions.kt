package com.civilcam.ui.terms.model

import com.civilcam.domainLayer.model.docs.TermsType
import com.civilcam.ext_features.compose.ComposeFragmentActions

sealed class TermsActions : ComposeFragmentActions {
    object ClickGoBack : TermsActions()
    object ClickContinue : TermsActions()
    data class ClickDocument(val webLink: TermsType) : TermsActions()
    object ClickCloseAlert : TermsActions()

}
