package com.civilcam.ui.subscription

import androidx.lifecycle.viewModelScope
import com.civilcam.domainLayer.ServiceException
import com.civilcam.domainLayer.castSafe
import com.civilcam.domainLayer.model.SubscriptionType
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
	private val isReselect: Boolean,
	private val getSubscriptionsUseCase: GetSubscriptionsUseCase,
	private val setUserSubscriptionUseCase: SetUserSubscriptionUseCase,
	private val getSubscriptionUseCase: GetUserSubscriptionUseCase
) : ComposeViewModel<SubscriptionState, SubscriptionRoute, SubscriptionActions>() {
	override var _state: MutableStateFlow<SubscriptionState> = MutableStateFlow(SubscriptionState())
	
	init {
		_state.update { it.copy(isReselect = isReselect) }
		getCurrentSubscription()
	}
	
	override fun setInputActions(action: SubscriptionActions) {
		when (action) {
			SubscriptionActions.GoBack -> goBack()
			is SubscriptionActions.OnSubSelect -> onSubSelected(action.title)
			SubscriptionActions.GoStart -> goStart()
			SubscriptionActions.GoProfileSetup -> goProfileSetup()
		}
	}
	
	private fun getSubscriptions() {
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
					error.castSafe<ServiceException>()?.let {
						_state.update { it.copy(errorText = it.errorText) }
					}
				}
			_state.value = _state.value.copy(isLoading = false)
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
			if (_state.value.isReselect) {
				SubscriptionRoute.GoBack
			} else {
				SubscriptionRoute.GoProfileSetup
			}
		)
		_state.update { it.copy(purchaseFail = false, purchaseSuccess = false) }
	}
	
	private fun goBack() {
		navigateRoute(SubscriptionRoute.GoBack)
	}
	
	private fun goStart() {
		//just for ui test
		if (_state.value.selectedSubscriptionType == TRIAL) {
			_state.value = _state.value.copy(purchaseFail = true)
		} else {
			_state.value = _state.value.copy(purchaseSuccess = true)
		}
	}

	private fun onSubSelected(subscriptionType: String) {
		_state.value = _state.value.copy(selectedSubscriptionType = subscriptionType)
	}

	override fun clearErrorText() {

	}
	
	companion object {
		private const val TRIAL = "Trial"
	}
}