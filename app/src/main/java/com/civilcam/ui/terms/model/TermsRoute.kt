package com.civilcam.ui.terms.model

import com.civilcam.domain.model.TermsType

sealed class TermsRoute {
    object GoSubscription : TermsRoute()
    object GoBack : TermsRoute()
    data class GoWebView(val webLink: TermsType) : TermsRoute()
}
