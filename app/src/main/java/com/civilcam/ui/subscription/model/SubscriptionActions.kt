package com.civilcam.ui.subscription.model

import com.android.billingclient.api.ProductDetails
import com.civilcam.ext_features.compose.ComposeFragmentActions

sealed class SubscriptionActions : ComposeFragmentActions {
	object GoBack : SubscriptionActions()
	data class OnSubSelect(val title: String) : SubscriptionActions()

	//	object GoProfileSetup : SubscriptionActions()
//	object ClearErrorText : SubscriptionActions()
	data class CloseAlert(val alertDecision: Boolean) : SubscriptionActions()
	data class SetProductList(val list: List<ProductDetails>) : SubscriptionActions()
	data class SetPurchaseToken(val token: String) : SubscriptionActions()

}