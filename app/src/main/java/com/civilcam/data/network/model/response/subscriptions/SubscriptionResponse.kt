package com.civilcam.data.network.model.response.subscriptions

import com.civilcam.domainLayer.model.SubscriptionStatus
import com.google.gson.annotations.SerializedName

class SubscriptionResponse(
	@SerializedName("subscription") val subscription: UserSubscriptionResponse
) {
	
	class UserSubscriptionResponse(
		@SerializedName("id") val id: Int,
		@SerializedName("productId") val productId: String?,
		@SerializedName("title") val title: String,
		@SerializedName("cost") val cost: Int,
		@SerializedName("term") val term: Int,
		@SerializedName("unitType") val unitType: String,
		@SerializedName("expiredAt") val expiredAt: String,
		@SerializedName("status") val status: SubscriptionStatus
	)
}

