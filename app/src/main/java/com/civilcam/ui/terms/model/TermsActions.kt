package com.civilcam.ui.terms.model

import com.civilcam.common.ext.compose.ComposeFragmentActions
import com.civilcam.domainLayer.model.docs.TermsType

sealed class TermsActions : ComposeFragmentActions {
    object ClickGoBack : TermsActions()
    object ClickContinue : TermsActions()
    data class ClickDocument(val webLink: TermsType) : TermsActions()
    object ClickCloseAlert : TermsActions()

}
