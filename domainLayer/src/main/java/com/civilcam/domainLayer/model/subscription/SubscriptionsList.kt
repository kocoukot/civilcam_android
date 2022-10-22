package com.civilcam.domainLayer.model.subscription

data class SubscriptionsList(
	val list: List<SubscriptionInfo>
) {
	
	data class SubscriptionInfo(
		val productId: String,
		val title: String,
		val description: String,
		val cost: Float,
		val term: Float,
		val unitType: String
	)
	
}