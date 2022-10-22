package com.civilcam.data.network.model.request.user

import com.google.gson.annotations.SerializedName

class FCMTokenRequest(
    @SerializedName("fcmToken") private val fcmToken: String,
)