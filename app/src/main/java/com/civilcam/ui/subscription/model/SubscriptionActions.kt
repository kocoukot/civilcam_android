package com.civilcam.ui.subscription.model

import com.civilcam.common.ext.compose.ComposeFragmentActions

sealed class SubscriptionActions : ComposeFragmentActions{
	object GoBack : SubscriptionActions()
}