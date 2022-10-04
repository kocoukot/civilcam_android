package com.civilcam.data.network.model.request.subscriptions

import com.google.gson.annotations.SerializedName

class SubscriptionRequest(
	@SerializedName("receipt") val receipt: String,
	@SerializedName("productId") val productId: String
)