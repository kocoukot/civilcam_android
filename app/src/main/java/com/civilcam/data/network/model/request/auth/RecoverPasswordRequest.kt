package com.civilcam.data.network.model.request.auth

import com.google.gson.annotations.SerializedName

class RecoverPasswordRequest(
	@SerializedName("recoveryToken") private val recoveryToken: String,
	@SerializedName("newPassword") private val newPassword: String
)