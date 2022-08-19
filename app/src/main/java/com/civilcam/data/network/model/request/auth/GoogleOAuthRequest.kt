package com.civilcam.data.network.model.request.auth

import com.google.gson.annotations.SerializedName

class GoogleOAuthRequest(
    @SerializedName("code") val code: String,
    @SerializedName("grant_type") val grantType: String = "authorization_code",
    @SerializedName("client_id") val clientId: String = "403034509114-8e3bvocoju9cn67uhuk7ktce39no0erb.apps.googleusercontent.com",    //uildConfig.GOOGLE_WEB_CLIENT_ID,
    @SerializedName("client_secret") val clientSecret: String = "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",   //BuildConfig.GOOGLE_CLIENT_SECRET,
    @SerializedName("redirect_uri") val redirect_uri: String = "",
)