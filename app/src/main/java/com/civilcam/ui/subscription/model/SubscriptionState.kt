package com.civilcam.ui.subscription.model

import com.civilcam.ext_features.compose.ComposeFragmentState
import com.civilcam.domainLayer.model.SubscriptionType
import com.civilcam.domainLayer.model.subscription.SubscriptionsList

data class SubscriptionState(
	val isLoading: Boolean = false,
	val errorText: String = "",
	val isReselect: Boolean = false,
	val selectedSubscriptionType: SubscriptionType = SubscriptionType.TRIAL,
	val subscriptionsList: SubscriptionsList = SubscriptionsList(list = listOf()),
	val purchaseSuccess: Boolean = false,
	val purchaseFail: Boolean = false
) : ComposeFragmentState