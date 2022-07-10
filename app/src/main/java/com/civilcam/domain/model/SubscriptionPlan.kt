package com.civilcam.domain.model

data class SubscriptionPlan(
	val subscriptionPeriod: String = "",
	val subscriptionPlan: String = "",
	val purchasePrice: String = "",
	val autoRenewDate: String = ""
)