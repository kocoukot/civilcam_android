package com.civilcam.ui.subscription.model

import com.civilcam.domainLayer.model.SubscriptionType
import com.civilcam.ext_features.compose.ComposeFragmentActions

sealed class SubscriptionActions : ComposeFragmentActions{
	object GoBack : SubscriptionActions()
    data class OnSubSelect(val type: SubscriptionType) : SubscriptionActions()
	object GoStart : SubscriptionActions()
	object GoProfileSetup : SubscriptionActions()
}