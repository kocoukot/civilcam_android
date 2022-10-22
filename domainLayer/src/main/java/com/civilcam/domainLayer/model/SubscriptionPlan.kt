package com.civilcam.domainLayer.model

data class SubscriptionPlan(
	val subscriptionPeriod: String = "",
	val subscriptionPlan: String = "",
	val autoRenewDate: String = ""
)