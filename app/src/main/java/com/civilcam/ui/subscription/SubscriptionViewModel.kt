package com.civilcam.ui.subscription

import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.domain.usecase.GetSubscriptionPlansUseCase
import com.civilcam.ui.subscription.model.SubscriptionActions
import com.civilcam.ui.subscription.model.SubscriptionRoute
import com.civilcam.ui.subscription.model.SubscriptionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SubscriptionViewModel(
	private val getSubscriptionPlansUseCase: GetSubscriptionPlansUseCase
) :
	ComposeViewModel<SubscriptionState, SubscriptionRoute, SubscriptionActions>() {
	override var _state: MutableStateFlow<SubscriptionState> = MutableStateFlow(SubscriptionState())
	
	init {
		viewModelScope.launch {
			kotlin.runCatching {
				getSubscriptionPlansUseCase.getSubscriptionPlans()
			}
				.onSuccess {
					val response = getSubscriptionPlansUseCase.getSubscriptionPlans()
					_state.value = _state.value.copy(subscriptionPlans = response)
				}
				.onFailure {
				
				}
		}
	}
	
	override fun setInputActions(action: SubscriptionActions) {
		TODO("Not yet implemented")
	}
}