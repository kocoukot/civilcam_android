package com.civilcam.ui.subscription

import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.domain.model.SubscriptionType
import com.civilcam.ui.subscription.model.SubscriptionActions
import com.civilcam.ui.subscription.model.SubscriptionRoute
import com.civilcam.ui.subscription.model.SubscriptionState
import kotlinx.coroutines.flow.MutableStateFlow

class SubscriptionViewModel :
	ComposeViewModel<SubscriptionState, SubscriptionRoute, SubscriptionActions>() {
	override var _state: MutableStateFlow<SubscriptionState> = MutableStateFlow(SubscriptionState())
	
	override fun setInputActions(action: SubscriptionActions) {
		when (action) {
			SubscriptionActions.GoBack -> goBack()
			is SubscriptionActions.OnSubSelect -> onSubSelected(action.type)
			SubscriptionActions.GoStart -> goStart()
			SubscriptionActions.GoProfileSetup -> goProfileSetup()
		}
	}
	
	private fun goProfileSetup() {
		_steps.value = SubscriptionRoute.GoProfileSetup
		_state.value = _state.value.copy(purchaseFail = false)
		_state.value = _state.value.copy(purchaseSuccess = false)
	}
	
	private fun goBack() {
		_steps.value = SubscriptionRoute.GoBack
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