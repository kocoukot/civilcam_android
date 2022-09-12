package com.civilcam.ui.terms.model

import com.civilcam.ext_features.compose.ComposeFragmentRoute

sealed class TermsRoute : ComposeFragmentRoute {
    object GoSubscription : TermsRoute()
    object GoBack : TermsRoute()
    data class GoWebView(val webLink: String) : TermsRoute()
}
