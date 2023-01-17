package com.civilcam.data.network.model.response.auth

import com.civilcam.data.network.model.response.subscriptions.SubscriptionResponse
import com.civilcam.data.network.model.response.user.UserSettingsResponse
import com.google.gson.annotations.SerializedName

class UserResponse(
    @SerializedName("accessToken") val accessToken: String?,
    @SerializedName("sessionUser") val sessionUser: SessionUserResponse,
    @SerializedName("profile") val userProfile: UseBaseInfoResponse,
    @SerializedName("settings") val settings: UserSettingsResponse,
    @SerializedName("subscription") val subscription: SubscriptionResponse.UserSubscriptionResponse?,
)
