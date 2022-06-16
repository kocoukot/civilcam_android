package com.civilcam.ui.terms

import androidx.lifecycle.ViewModel
import com.civilcam.arch.common.livedata.SingleLiveEvent
import com.civilcam.domain.model.TermsType
import com.civilcam.ui.terms.model.TermsActions
import com.civilcam.ui.terms.model.TermsRoute
import com.civilcam.ui.terms.model.TermsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TermsViewModel(
    isSettings: Boolean
) : ViewModel() {

    private val _state: MutableStateFlow<TermsState> = MutableStateFlow(TermsState())
    val state: StateFlow<TermsState> = _state

    private val _steps: SingleLiveEvent<TermsRoute> = SingleLiveEvent()
    val steps: SingleLiveEvent<TermsRoute> = _steps

    init {
        _state.value = _state.value.copy(isSettings = isSettings)
    }

    fun setInputActions(action: TermsActions) {
        when (action) {
            TermsActions.CLickBack -> goBack()
            is TermsActions.ClickAccept -> {}
            TermsActions.ClickContinue -> goNext()
            is TermsActions.ClickDocument -> goWebView(action.webLink)
        }
    }

    private fun goBack() {
        _steps.value = TermsRoute.GoBack
    }

    private fun termsAccepted() {
//        _state.value = _state.value.copy(isTermsAccepted = )
    }

    private fun goNext() {
        _steps.value = TermsRoute.GoSubscription
    }

    private fun goWebView(webLink: TermsType) {
        _steps.value = TermsRoute.GoWebView(webLink)
    }
}
