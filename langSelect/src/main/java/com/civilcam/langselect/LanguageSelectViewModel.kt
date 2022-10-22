package com.civilcam.langselect

import androidx.lifecycle.viewModelScope
import com.civilcam.domainLayer.model.user.LanguageType
import com.civilcam.ext_features.compose.ComposeViewModel
import com.civilcam.langselect.model.LangSelectActions
import com.civilcam.langselect.model.LangSelectRoute
import com.civilcam.langselect.model.LangSelectState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LanguageSelectViewModel :
    ComposeViewModel<LangSelectState, LangSelectRoute, LangSelectActions>() {

    override var _state: MutableStateFlow<LangSelectState> = MutableStateFlow(LangSelectState())

    init {
        viewModelScope.launch {
            //   _state.update { it.copy(selectedLang = LocaleHelper.getSelectedLanguage()) }
        }
    }

    override fun setInputActions(action: LangSelectActions) {
        when (action) {
            is LangSelectActions.LanguageSelect -> languageSelect(action.language)
            LangSelectActions.ClickContinue -> toOnBoarding()
        }
    }

    private fun languageSelect(language: LanguageType) {
        _state.update { it.copy(selectedLang = language) }
    }

    private fun toOnBoarding() {
        navigateRoute(LangSelectRoute.ToOnBoarding)
    }

    override fun clearErrorText() {

    }
}