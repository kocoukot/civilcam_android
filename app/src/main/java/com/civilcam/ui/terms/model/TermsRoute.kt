package com.civilcam.ui.terms.model

import com.civilcam.common.ext.compose.ComposeFragmentRoute

sealed class TermsRoute : ComposeFragmentRoute {
    object GoSubscription : TermsRoute()
    object GoBack : TermsRoute()
    data class GoWebView(val webLink: String) : TermsRoute()
}
