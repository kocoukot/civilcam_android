package com.civilcam.ui.langSelect

import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.domain.model.LanguageType
import com.civilcam.ui.langSelect.model.LangSelectActions
import com.civilcam.ui.langSelect.model.LangSelectRoute
import com.civilcam.ui.langSelect.model.LangSelectState
import com.civilcam.utils.LocaleHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LanguageSelectViewModel :
    ComposeViewModel<LangSelectState, LangSelectRoute, LangSelectActions>() {

    override var _state: MutableStateFlow<LangSelectState> = MutableStateFlow(LangSelectState())

    init {
        viewModelScope.launch {
            val selectedLang = LocaleHelper.getSelectedLanguage()
            _state.value = _state.value.copy(selectedLang = selectedLang)
        }
    }

    override fun setInputActions(action: LangSelectActions) {
        when (action) {
            is LangSelectActions.LanguageSelect -> languageSelect(action.language)
            LangSelectActions.ClickContinue -> toOnBoarding()
        }
    }

    private fun languageSelect(language: LanguageType) {
        _state.value = _state.value.copy(selectedLang = language)
    }

    private fun toOnBoarding() {
        navigateRoute(LangSelectRoute.ToOnBoarding)
    }
}