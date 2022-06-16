package com.civilcam.ui.langSelect.model

import com.civilcam.domain.model.LanguageType

sealed class LangSelectActions {
    data class LanguageSelect(val language: LanguageType) : LangSelectActions()
    object ClickContinue : LangSelectActions()

}
