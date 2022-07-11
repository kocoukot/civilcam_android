package com.civilcam.domain.model

data class SubscriptionPlan(
	val subscriptionPeriod: String = "",
	val subscriptionPlan: String = "",
	val autoRenewDate: String = ""
)