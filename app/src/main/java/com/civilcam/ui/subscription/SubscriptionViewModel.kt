package com.civilcam.ui.subscription

import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.domain.model.SubscriptionType
import com.civilcam.ui.subscription.model.SubscriptionActions
import com.civilcam.ui.subscription.model.SubscriptionRoute
import com.civilcam.ui.subscription.model.SubscriptionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SubscriptionViewModel:
	ComposeViewModel<SubscriptionState, SubscriptionRoute, SubscriptionActions>() {
	override var _state: MutableStateFlow<SubscriptionState> = MutableStateFlow(SubscriptionState())
	
	init {
	
	}
	
	override fun setInputActions(action: SubscriptionActions) {
		when(action) {
			is SubscriptionActions.OnSubSelect -> onSubSelected(action.type)
		}
	}
	
	private fun onSubSelected(subscription: SubscriptionType) {
	
	}
}