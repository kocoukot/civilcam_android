package com.civilcam.ui.langSelect.model

import com.civilcam.common.ext.compose.ComposeFragmentRoute

sealed class LangSelectRoute : ComposeFragmentRoute {
    object ToOnBoarding : LangSelectRoute()
}

