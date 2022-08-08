package com.civilcam.ui.subscription.model

import com.civilcam.common.ext.compose.ComposeFragmentState

data class SubscriptionState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val isReselect: Boolean = false,
    val selectedSubscriptionType: com.civilcam.domainLayer.model.SubscriptionType = com.civilcam.domainLayer.model.SubscriptionType.TRIAL,
    val purchaseSuccess: Boolean = false,
    val purchaseFail: Boolean = false
) : ComposeFragmentState