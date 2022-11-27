package com.civilcam.ui.subscription

import androidx.lifecycle.viewModelScope
import com.civilcam.domainLayer.ServiceException
import com.civilcam.domainLayer.castSafe
import com.civilcam.domainLayer.model.subscription.UserSubscriptionState
import com.civilcam.domainLayer.serviceCast
import com.civilcam.domainLayer.usecase.subscriptions.GetSubscriptionsUseCase
import com.civilcam.domainLayer.usecase.subscriptions.GetUserSubscriptionUseCase
import com.civilcam.domainLayer.usecase.subscriptions.SetUserSubscriptionUseCase
import com.civilcam.ext_features.compose.ComposeViewModel
import com.civilcam.ui.subscription.model.SubscriptionActions
import com.civilcam.ui.subscription.model.SubscriptionRoute
import com.civilcam.ui.subscription.model.SubscriptionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SubscriptionViewModel(
	private val userSubState: UserSubscriptionState,
	private val getSubscriptionsUseCase: GetSubscriptionsUseCase,
	private val setUserSubscriptionUseCase: SetUserSubscriptionUseCase,
	private val getSubscriptionUseCase: GetUserSubscriptionUseCase
) : ComposeViewModel<SubscriptionState, SubscriptionRoute, SubscriptionActions>() {
	override var _state: MutableStateFlow<SubscriptionState> = MutableStateFlow(SubscriptionState())
	
	init {
		_state.update { it.copy(userSubState = userSubState) }
		getCurrentSubscription()
	}
	
	override fun setInputActions(action: SubscriptionActions) {
		when (action) {
			SubscriptionActions.GoBack -> goBack()
			is SubscriptionActions.OnSubSelect -> onSubSelected(action.title)
			SubscriptionActions.GoProfileSetup -> goProfileSetup()
			SubscriptionActions.ClearErrorText -> clearErrorText()
		}
	}
	
	private fun getSubscriptions() {
		_state.update { it.copy(isLoading = true) }
		viewModelScope.launch {
			kotlin.runCatching { getSubscriptionsUseCase.getSubscriptions() }
				.onSuccess { data ->
					_state.update {
						it.copy(
							subscriptionsList = data,
							isLoading = false
						)
					}
				}
				.onFailure { error ->
					error.serviceCast { errorText, _, _ ->
						_state.update { it.copy(errorText = errorText) }
					}
				}
			_state.update { it.copy(isLoading = false) }
		}
	}
	
	private fun getCurrentSubscription() {
		_state.value = _state.value.copy(isLoading = true)
		viewModelScope.launch {
			kotlin.runCatching { getSubscriptionUseCase.getUserSubscription() }
				.onSuccess { data ->
					_state.update {
						it.copy(
							subscription = data,
							selectedSubscriptionType = data.title
						)
					}
					getSubscriptions()
				}
				.onFailure { error ->
					error.castSafe<ServiceException>()?.let {
						_state.update { it.copy(errorText = it.errorText) }
					}
				}
			_state.value = _state.value.copy(isLoading = false)
		}
	}
	
	private fun goProfileSetup() {
		navigateRoute(
			when (userSubState) {
				UserSubscriptionState.FIRST_LAUNCH -> SubscriptionRoute.GoProfileSetup
				UserSubscriptionState.SUB_RESELECT -> SubscriptionRoute.GoBack
				UserSubscriptionState.SUB_EXPIRED -> SubscriptionRoute.GoMap
			}
		)
		_state.update { it.copy(purchaseSuccess = false) }
	}
	
	private fun goBack() {
		navigateRoute(SubscriptionRoute.GoBack)
	}

	private fun onSubSelected(subscriptionType: String) {
		//todo api request for subscription
//		if (_state.value.selectedSubscriptionType == TRIAL) {
//		} else {
		_state.value = _state.value.copy(purchaseSuccess = true)
//		}
//		_state.value = _state.value.copy(selectedSubscriptionType = subscriptionType)
	}

	override fun clearErrorText() {
		_state.update { it.copy(errorText = "") }
	}
	
	companion object {
		private const val TRIAL = "Trial"
	}
}