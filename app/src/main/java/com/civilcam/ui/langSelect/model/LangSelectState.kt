package com.civilcam.ui.langSelect.model

import com.civilcam.common.ext.compose.ComposeFragmentState
import com.civilcam.domain.model.LanguageType

data class LangSelectState(
    val selectedLang: LanguageType = LanguageType.ENGLISH
) : ComposeFragmentState

