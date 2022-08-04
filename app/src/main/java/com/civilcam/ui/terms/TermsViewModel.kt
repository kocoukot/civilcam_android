package com.civilcam.ui.terms

import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.data.network.support.ServiceException
import com.civilcam.domain.model.TermsType
import com.civilcam.domain.usecase.docs.GetTermsLinksUseCase
import com.civilcam.domain.usecase.user.AcceptLegalDocsUseCase
import com.civilcam.ui.terms.model.TermsActions
import com.civilcam.ui.terms.model.TermsRoute
import com.civilcam.ui.terms.model.TermsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TermsViewModel(
    isSettings: Boolean,
    getTermsLinksUseCase: GetTermsLinksUseCase,
    private val acceptLegalDocsUseCase: AcceptLegalDocsUseCase
) : ComposeViewModel<TermsState, TermsRoute, TermsActions>() {

    override var _state = MutableStateFlow(TermsState())

    init {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            kotlin.runCatching { getTermsLinksUseCase.invoke() }
                .onSuccess { docs ->
                    _state.update { it.copy(isSettings = isSettings, legalDocs = docs) }
                }
                .onFailure { error ->
                    error as ServiceException
                    _state.update { it.copy(errorText = error.errorMessage) }
                }
            _state.update { it.copy(isLoading = false) }
        }
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
        viewModelScope.launch {
            kotlin.runCatching { acceptLegalDocsUseCase.invoke() }
                .onSuccess {
                    _steps.value = TermsRoute.GoSubscription
                }
        }
    }

    private fun goWebView(webLink: TermsType) {
        _steps.value = TermsRoute.GoWebView(webLink.name)
    }


}
