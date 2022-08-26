package com.civilcam.data.network.model.request.auth

import com.civilcam.BuildConfig
import com.google.gson.annotations.SerializedName

class GoogleOAuthRequest(
    @SerializedName("code") private val code: String,
    @SerializedName("grant_type") private val grantType: String = "authorization_code",
    @SerializedName("client_id") private val clientId: String = BuildConfig.GOOGLE_WEB_CLIENT_ID,
    @SerializedName("client_secret") private val clientSecret: String = BuildConfig.GOOGLE_CLIENT_SECRET,
    @SerializedName("redirect_uri") private val redirect_uri: String = "https://api.staging.civilcam.cloud",
    @SerializedName("scope") private val scope: String = "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile",
)