package com.civilcam.data.network.model.request.auth

import com.civilcam.BuildConfig
import com.google.gson.annotations.SerializedName

class GoogleOAuthRequest(
    @SerializedName("code") val code: String,
    @SerializedName("grant_type") val grantType: String = "authorization_code",
    @SerializedName("client_id") val clientId: String = BuildConfig.GOOGLE_WEB_CLIENT_ID,
    @SerializedName("client_secret") val clientSecret: String = BuildConfig.GOOGLE_CLIENT_SECRET,
    @SerializedName("redirect_uri") val redirect_uri: String = "",
)