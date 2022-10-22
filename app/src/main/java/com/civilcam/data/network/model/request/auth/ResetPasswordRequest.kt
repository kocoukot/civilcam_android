package com.civilcam.data.network.model.request.auth

import com.google.gson.annotations.SerializedName

class ResetPasswordRequest(
	@SerializedName("email") private val email: String
)