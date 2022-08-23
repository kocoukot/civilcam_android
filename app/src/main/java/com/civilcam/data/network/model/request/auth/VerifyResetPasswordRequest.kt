package com.civilcam.data.network.model.request.auth

import com.google.gson.annotations.SerializedName

class VerifyResetPasswordRequest(
	@SerializedName("email") private val email: String,
	@SerializedName("code") private val code: String
)