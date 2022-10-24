package com.civilcam.langselect

import com.civilcam.domainLayer.model.user.LanguageType
import com.civilcam.ext_features.arch.BaseViewModel
import com.civilcam.langselect.model.LangSelectActions
import com.civilcam.langselect.model.LangSelectRoute
import com.civilcam.langselect.model.LangSelectState
import kotlinx.coroutines.flow.MutableStateFlow

class LanguageSelectViewModel :
    BaseViewModel.Base<LangSelectState, LangSelectActions, LangSelectRoute>(
        mState = MutableStateFlow(LangSelectState())
    ) {


    override fun setInputActions(action: LangSelectActions) {
        when (action) {
            is LangSelectActions.LanguageSelect -> languageSelect(action.language)
            LangSelectActions.ClickContinue -> toOnBoarding()
        }
    }

    private fun languageSelect(language: LanguageType) {
        updateInfo { copy(selectedLang = language) }
    }

    private fun toOnBoarding() {
        sendEvent(LangSelectRoute.ToOnBoarding)
    }

    override fun clearErrorText() {}
}