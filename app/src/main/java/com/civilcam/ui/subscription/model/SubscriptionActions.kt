package com.civilcam.ui.subscription.model

import com.civilcam.common.ext.compose.ComposeFragmentActions

sealed class SubscriptionActions : ComposeFragmentActions{
	object GoBack : SubscriptionActions()
	data class OnSubSelect(val type: com.civilcam.domainLayer.model.SubscriptionType) : SubscriptionActions()
	object GoStart : SubscriptionActions()
	object GoProfileSetup : SubscriptionActions()
}