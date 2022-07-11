package com.civilcam.ui.subscription.model

import com.civilcam.common.ext.compose.ComposeFragmentState

data class SubscriptionState(
	val isLoading: Boolean = false,
	val errorText: String = "",
) : ComposeFragmentState