package com.civilcam.ui.subscription

import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.domain.model.SubscriptionType
import com.civilcam.ui.subscription.model.SubscriptionActions
import com.civilcam.ui.subscription.model.SubscriptionRoute
import com.civilcam.ui.subscription.model.SubscriptionState
import kotlinx.coroutines.flow.MutableStateFlow

class SubscriptionViewModel(
	private val isReselect: Boolean
) :
	ComposeViewModel<SubscriptionState, SubscriptionRoute, SubscriptionActions>() {
	override var _state: MutableStateFlow<SubscriptionState> = MutableStateFlow(SubscriptionState())
	
	init {
		_state.value = _state.value.copy(isReselect = isReselect)
	}
	
	override fun setInputActions(action: SubscriptionActions) {
		when (action) {
			SubscriptionActions.GoBack -> goBack()
			is SubscriptionActions.OnSubSelect -> onSubSelected(action.type)
			SubscriptionActions.GoStart -> goStart()
			SubscriptionActions.GoProfileSetup -> goProfileSetup()
		}
	}
	
	private fun goProfileSetup() {
		if (_state.value.isReselect) {
			navigateRoute(SubscriptionRoute.GoBack)
		} else {
			navigateRoute(SubscriptionRoute.GoProfileSetup)
		}
		_state.value = _state.value.copy(purchaseFail = false)
		_state.value = _state.value.copy(purchaseSuccess = false)
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
}