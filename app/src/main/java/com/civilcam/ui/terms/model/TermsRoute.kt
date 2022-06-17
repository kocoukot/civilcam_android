package com.civilcam.ui.terms.model

sealed class TermsRoute {
    object GoSubscription : TermsRoute()
    object GoBack : TermsRoute()
    data class GoWebView(val webLink: String) : TermsRoute()
}
