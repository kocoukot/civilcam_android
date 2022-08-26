package com.civilcam.data.network.model.request.user

import com.google.gson.annotations.SerializedName

class ChangeEmailRequest(
    @SerializedName("currentEmail") private val currentEmail: String,
    @SerializedName("newEmail") private val newEmail: String
)