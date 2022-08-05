package com.civilcam.ui.langSelect.model

import com.civilcam.common.ext.compose.ComposeFragmentActions
import com.civilcam.domain.model.LanguageType

sealed class LangSelectActions : ComposeFragmentActions {
    data class LanguageSelect(val language: LanguageType) : LangSelectActions()
    object ClickContinue : LangSelectActions()

}
