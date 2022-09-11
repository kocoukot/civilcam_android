package com.civilcam.ui.langSelect.model

import com.civilcam.domainLayer.model.user.LanguageType
import com.civilcam.ext_features.compose.ComposeFragmentActions

sealed class LangSelectActions : ComposeFragmentActions {
    data class LanguageSelect(val language: LanguageType) : LangSelectActions()
    object ClickContinue : LangSelectActions()
}