package com.civilcam.data.network.model.request.verify

import com.google.gson.annotations.SerializedName

class OTPTypeRequest(
    @SerializedName("otpType") private val otpType: String,
)
