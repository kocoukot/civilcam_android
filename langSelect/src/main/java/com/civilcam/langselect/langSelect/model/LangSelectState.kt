package com.civilcam.langselect.langSelect.model

import com.civilcam.domainLayer.model.user.LanguageType
import com.civilcam.ext_features.compose.ComposeFragmentState

data class LangSelectState(
    val selectedLang: LanguageType = LanguageType.en
) : ComposeFragmentState

