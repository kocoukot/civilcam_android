package com.civilcam.ui.subscription.model

import com.civilcam.ext_features.compose.ComposeFragmentRoute

sealed class SubscriptionRoute : ComposeFragmentRoute {
	object GoBack : SubscriptionRoute()
	object GoProfileSetup : SubscriptionRoute()
}