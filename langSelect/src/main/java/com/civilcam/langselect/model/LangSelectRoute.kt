package com.civilcam.langselect.model

import com.civilcam.ext_features.compose.ComposeFragmentRoute

sealed class LangSelectRoute : ComposeFragmentRoute {
    object ToOnBoarding : LangSelectRoute()
}