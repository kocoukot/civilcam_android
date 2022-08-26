package com.civilcam.data.network.model.request.auth

import com.google.gson.annotations.SerializedName

class SocialLoginRequest(
    @SerializedName("accessToken") private val accessToken: String
)