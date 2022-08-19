package com.civilcam.data.network.model.response.auth

import com.google.gson.annotations.SerializedName

class GoogleOAuthResponse(
    @SerializedName("expires_in") val expiresIn: Int,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("id_token") val idToken: String,
    @SerializedName("access_token") val accessToken: String
)
