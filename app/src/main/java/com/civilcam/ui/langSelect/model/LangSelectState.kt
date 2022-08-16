package com.civilcam.ui.langSelect.model

import com.civilcam.common.ext.compose.ComposeFragmentState
import com.civilcam.domainLayer.model.LanguageType

data class LangSelectState(
    val selectedLang: LanguageType = LanguageType.en
) : ComposeFragmentState

