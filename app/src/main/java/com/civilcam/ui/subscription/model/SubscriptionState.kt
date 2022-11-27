package com.civilcam.ui.subscription.model

import com.civilcam.domainLayer.model.subscription.SubscriptionBaseInfo
import com.civilcam.domainLayer.model.subscription.SubscriptionsList
import com.civilcam.domainLayer.model.subscription.UserSubscriptionState
import com.civilcam.ext_features.compose.ComposeFragmentState

data class SubscriptionState(
	val isLoading: Boolean = false,
	val errorText: String = "",
	val userSubState: UserSubscriptionState = UserSubscriptionState.FIRST_LAUNCH,
	val selectedSubscriptionType: String = "Trial",
	val subscriptionsList: SubscriptionsList = SubscriptionsList(list = listOf()),
	val subscription: SubscriptionBaseInfo = SubscriptionBaseInfo(),
	val purchaseSuccess: Boolean = false,
	//val purchaseFail: Boolean = false
) : ComposeFragmentState