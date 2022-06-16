package com.civilcam.ui.langSelect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.civilcam.arch.common.livedata.SingleLiveEvent
import com.civilcam.domain.model.LanguageType
import com.civilcam.ui.langSelect.model.LangSelectActions
import com.civilcam.ui.langSelect.model.LangSelectRoute
import com.civilcam.ui.langSelect.model.LangSelectState
import com.civilcam.utils.LocaleHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LanguageSelectViewModel : ViewModel() {


    private val _state: MutableStateFlow<LangSelectState> = MutableStateFlow(LangSelectState())
    val state: StateFlow<LangSelectState> = _state

    private val _steps: SingleLiveEvent<LangSelectRoute> = SingleLiveEvent()
    val steps: SingleLiveEvent<LangSelectRoute> = _steps

    init {
        viewModelScope.launch {
            val selectedLang = LocaleHelper.getSelectedLanguage()
            _state.value = _state.value.copy(selectedLang = selectedLang)
        }

    }

    fun setInputAction(action: LangSelectActions) {
        when (action) {
            is LangSelectActions.LanguageSelect -> languageSelect(action.language)
            LangSelectActions.ClickContinue -> toOnBoarding()
        }
    }


    private fun languageSelect(language: LanguageType) {
        _state.value = _state.value.copy(selectedLang = language)
    }

    private fun toOnBoarding() {
        _steps.value = LangSelectRoute.ToOnBoarding
    }
}