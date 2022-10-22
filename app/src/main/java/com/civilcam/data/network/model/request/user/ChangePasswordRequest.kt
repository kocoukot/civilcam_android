package com.civilcam.data.network.model.request.user

import com.google.gson.annotations.SerializedName

class ChangePasswordRequest(
    @SerializedName("currentPassword") private val currentPassword: String,
    @SerializedName("newPassword") private val newPassword: String
)