package com.civilcam.data.network.model.response.auth

import com.google.gson.annotations.SerializedName

class UseBaseInfoResponse(
    @SerializedName("avatar") val avatar: String?,
    @SerializedName("firstName") val firstName: String?,
    @SerializedName("lastName") val lastName: String?,
    @SerializedName("dob") val dob: String?,
    @SerializedName("address") val address: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("isPhoneVerified") val isPhoneVerified: Boolean = false,
)