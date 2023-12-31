package com.civilcam.ui.terms

import androidx.lifecycle.viewModelScope
import com.civilcam.domainLayer.model.docs.TermsType
import com.civilcam.domainLayer.serviceCast
import com.civilcam.domainLayer.usecase.docs.GetTermsLinksUseCase
import com.civilcam.domainLayer.usecase.user.AcceptLegalDocsUseCase
import com.civilcam.domainLayer.usecase.user.GetLocalCurrentUserUseCase
import com.civilcam.ext_features.compose.ComposeViewModel
import com.civilcam.ui.terms.model.TermsActions
import com.civilcam.ui.terms.model.TermsRoute
import com.civilcam.ui.terms.model.TermsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TermsViewModel(
	isSettings: Boolean,
	getTermsLinksUseCase: GetTermsLinksUseCase,
	private val acceptLegalDocsUseCase: AcceptLegalDocsUseCase,
	private val getLocalCurrentUserUseCase: GetLocalCurrentUserUseCase
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
					error.serviceCast { msg, _, isForceLogout ->
						_state.update { it.copy(errorText = msg) }
					}
				}
			_state.update { it.copy(isLoading = false) }
		}
	}
	
	override fun setInputActions(action: TermsActions) {
		when (action) {
			TermsActions.ClickGoBack -> goBack()
			TermsActions.ClickContinue -> goNext()
			is TermsActions.ClickDocument -> goWebView(action.webLink)
			TermsActions.ClickCloseAlert -> clearErrorText()
		}
	}
	
	private fun goBack() {
		navigateRoute(TermsRoute.GoBack)
	}
	
	private fun goNext() {
		viewModelScope.launch {
			kotlin.runCatching { acceptLegalDocsUseCase.invoke() }
				.onSuccess {
					navigateRoute(
						TermsRoute.GoSubscription
					)
				}
				.onFailure { error ->
					error.serviceCast { msg, _, isForceLogout ->
						_state.update { it.copy(errorText = msg) }
					}
				}
		}
	}
	
	private fun goWebView(webLink: TermsType) {
		_state.value.legalDocs?.let {
			navigateRoute(
				TermsRoute.GoWebView(
					when (webLink) {
						TermsType.TERMS_CONDITIONS -> it.termsAndConditions
						TermsType.PRIVACY_POLICY -> it.privacyPolicy
					}
				)
			)
		}
	}
	
	override fun clearErrorText() {
		_state.update { it.copy(errorText = "") }
	}
}