package com.civilcam.data.network.model.response.subscriptions

import com.google.gson.annotations.SerializedName

class SubscriptionsListResponse(
    @SerializedName("list") val subsList: List<SubscriptionResponse>,
) {
    class SubscriptionResponse(
        @SerializedName("productId") val productId: String,
        @SerializedName("title") val title: String,
        @SerializedName("description") val description: String,
        @SerializedName("cost") val cost: Float,
        @SerializedName("term") val term: Float,
        @SerializedName("unitType") val unitType: String,
    )
}