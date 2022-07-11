package com.civilcam.ui.subscription.model

import com.civilcam.common.ext.compose.ComposeFragmentState
import com.civilcam.domain.model.SubscriptionType

data class SubscriptionState(
	val isLoading: Boolean = false,
	val errorText: String = "",
	val isReselect: Boolean = false,
	val selectedSubscriptionType: SubscriptionType = SubscriptionType.TRIAL,
	val purchaseSuccess: Boolean = false,
	val purchaseFail: Boolean = false
) : ComposeFragmentState