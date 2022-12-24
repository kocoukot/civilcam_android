package com.civilcam.ui.subscription.model

import com.android.billingclient.api.ProductDetails
import com.civilcam.ext_features.compose.ComposeFragmentRoute

sealed class SubscriptionRoute : ComposeFragmentRoute {
	object GoBack : SubscriptionRoute()
	object GoProfileSetup : SubscriptionRoute()
	object GoMap : SubscriptionRoute()
	object GoCreateAccount : SubscriptionRoute()
	object GoPinCode : SubscriptionRoute()
	data class LaunchPurchase(val purchase: ProductDetails) : SubscriptionRoute()

}