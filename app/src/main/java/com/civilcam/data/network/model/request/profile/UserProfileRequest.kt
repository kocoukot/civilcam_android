package com.civilcam.data.network.model.request.profile

import com.google.gson.annotations.SerializedName

class UserProfileRequest(
    @SerializedName("firstName") private val firstName: String?,
    @SerializedName("lastName") private val lastName: String?,
    @SerializedName("dob") private val dob: String?,
    @SerializedName("phone") private val phone: String?,
    @SerializedName("address") private val address: String?,
)