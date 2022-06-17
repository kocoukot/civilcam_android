package com.civilcam.ui.terms.model

import com.civilcam.domain.model.TermsType

sealed class TermsActions {
    object CLickBack : TermsActions()
    object ClickContinue : TermsActions()
    data class ClickAccept(val isAccepted: Boolean) : TermsActions()
    data class ClickDocument(val webLink: TermsType) : TermsActions()
}
