package com.civilcam.data.network.model.request.auth

import com.google.gson.annotations.SerializedName

class SignUpRequest(
    @SerializedName("email") private val email: String,
    @SerializedName("password") private val password: String,
)