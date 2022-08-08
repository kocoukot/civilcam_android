package com.civilcam.ui.langSelect.model

import com.civilcam.common.ext.compose.ComposeFragmentActions

sealed class LangSelectActions : ComposeFragmentActions {
    data class LanguageSelect(val language: com.civilcam.domainLayer.model.LanguageType) : LangSelectActions()
    object ClickContinue : LangSelectActions()

}
