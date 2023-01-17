package com.civilcam.data.network.model.request.subscriptions

import com.google.gson.annotations.SerializedName

class TrialSubscriptionRequest(
	@SerializedName("productId") val productId: String
)