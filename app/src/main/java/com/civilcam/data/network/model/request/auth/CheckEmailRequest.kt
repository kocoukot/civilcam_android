package com.civilcam.data.network.model.request.auth

import com.google.gson.annotations.SerializedName

class CheckEmailRequest(
    @SerializedName("email") private val email: String
)