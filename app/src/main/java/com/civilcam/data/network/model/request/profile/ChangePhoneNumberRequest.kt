package com.civilcam.data.network.model.request.profile

import com.google.gson.annotations.SerializedName

class ChangePhoneNumberRequest(
    @SerializedName("phone") private val phone: String
)