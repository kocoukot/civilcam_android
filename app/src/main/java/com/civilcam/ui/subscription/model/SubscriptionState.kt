package com.civilcam.ui.subscription.model

import com.civilcam.common.ext.compose.ComposeFragmentState
import com.civilcam.domain.model.SubscriptionPlan

data class SubscriptionState(
	val isLoading: Boolean = false,
	val errorText: String = "",
	val subscriptionPlans: List<SubscriptionPlan> = mutableListOf()
) : ComposeFragmentState