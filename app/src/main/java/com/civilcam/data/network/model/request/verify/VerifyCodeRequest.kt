package com.civilcam.data.network.model.request.verify

import com.google.gson.annotations.SerializedName

class VerifyCodeRequest(
    @SerializedName("otpType") private val otpType: String,
    @SerializedName("code") private val code: String,
)
