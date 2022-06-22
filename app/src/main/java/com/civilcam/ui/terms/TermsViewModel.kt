package com.civilcam.ui.terms

import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.domain.model.TermsType
import com.civilcam.ui.terms.model.TermsActions
import com.civilcam.ui.terms.model.TermsRoute
import com.civilcam.ui.terms.model.TermsState
import kotlinx.coroutines.flow.MutableStateFlow

class TermsViewModel(
    isSettings: Boolean
) : ComposeViewModel<TermsState, TermsRoute, TermsActions>() {

    override var _state = MutableStateFlow(TermsState())

    init {
        _state.value = _state.value.copy(isSettings = isSettings)
    }

    override fun setInputActions(action: TermsActions) {
        when (action) {
            TermsActions.ClickGoBack -> goBack()
            TermsActions.ClickContinue -> goNext()
            is TermsActions.ClickDocument -> goWebView(action.webLink)
        }
    }

    private fun goBack() {
        _steps.value = TermsRoute.GoBack
    }

    private fun goNext() {
        _steps.value = TermsRoute.GoSubscription
    }

    private fun goWebView(webLink: TermsType) {
        _steps.value = TermsRoute.GoWebView(webLink.name)
    }


}
