package com.civilcam.ui.langSelect.model

import com.civilcam.common.ext.compose.ComposeFragmentState

data class LangSelectState(
    val selectedLang: com.civilcam.domainLayer.model.LanguageType = com.civilcam.domainLayer.model.LanguageType.ENGLISH
) : ComposeFragmentState

