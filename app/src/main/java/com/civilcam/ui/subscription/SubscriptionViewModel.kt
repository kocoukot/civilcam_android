package com.civilcam.ui.subscription

import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.castSafe
import com.civilcam.ext_features.compose.ComposeViewModel
import com.civilcam.data.network.support.ServiceException
import com.civilcam.domainLayer.model.SubscriptionType
import com.civilcam.domainLayer.usecase.subscriptions.GetSubscriptionsUseCase
import com.civilcam.ui.subscription.model.SubscriptionActions
import com.civilcam.ui.subscription.model.SubscriptionRoute
import com.civilcam.ui.subscription.model.SubscriptionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SubscriptionViewModel(
	private val isReselect: Boolean,
	private val getSubscriptionsUseCase: GetSubscriptionsUseCase
) : ComposeViewModel<SubscriptionState, SubscriptionRoute, SubscriptionActions>() {
	override var _state: MutableStateFlow<SubscriptionState> = MutableStateFlow(SubscriptionState())
	
	init {
		_state.update { it.copy(isReselect = isReselect) }
		getSubscriptions()
	}
	
	override fun setInputActions(action: SubscriptionActions) {
		when (action) {
			SubscriptionActions.GoBack -> goBack()
			is SubscriptionActions.OnSubSelect -> onSubSelected(action.type)
			SubscriptionActions.GoStart -> goStart()
			SubscriptionActions.GoProfileSetup -> goProfileSetup()
		}
	}
	
	private fun getSubscriptions() {
		_state.value = _state.value.copy(isLoading = true)
		viewModelScope.launch {
			kotlin.runCatching { getSubscriptionsUseCase.getSubscriptions() }
				.onSuccess { data ->
					_state.update {
						it.copy(
							subscriptionsList = data
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
		if (_state.value.selectedSubscriptionType == SubscriptionType.TRIAL) {
			_state.value = _state.value.copy(purchaseFail = true)
		} else {
			_state.value = _state.value.copy(purchaseSuccess = true)
		}
	}

	private fun onSubSelected(subscriptionType: SubscriptionType) {
		_state.value = _state.value.copy(selectedSubscriptionType = subscriptionType)
	}

	override fun clearErrorText() {

	}
}