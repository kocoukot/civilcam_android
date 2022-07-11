package com.civilcam.ui.subscription.model

import com.civilcam.common.ext.compose.ComposeFragmentActions
import com.civilcam.domain.model.SubscriptionType

sealed class SubscriptionActions : ComposeFragmentActions{
	object GoBack : SubscriptionActions()
	data class OnSubSelect(val type: SubscriptionType) : SubscriptionActions()
	object GoStart : SubscriptionActions()
	object GoProfileSetup : SubscriptionActions()
}