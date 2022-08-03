package com.civilcam.data.network.model.response.auth

import com.google.gson.annotations.SerializedName

class UserResponse(
    @SerializedName("accessToken") val accessToken: String?,
    @SerializedName("sessionUser") val sessionUser: SessionUserResponse,
    @SerializedName("profile") val userProfile: UseBaseInfoResponse,
)