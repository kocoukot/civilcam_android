package com.civilcam.ui.subscription.model

import com.civilcam.ext_features.compose.ComposeFragmentActions

sealed class SubscriptionActions : ComposeFragmentActions {
	object GoBack : SubscriptionActions()
	data class OnSubSelect(val title: String) : SubscriptionActions()
	object GoProfileSetup : SubscriptionActions()
	object ClearErrorText : SubscriptionActions()
}