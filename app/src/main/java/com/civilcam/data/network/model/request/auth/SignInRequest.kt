package com.civilcam.data.network.model.request.auth

import com.google.gson.annotations.SerializedName

class SignInRequest(
	@SerializedName("email") val email: String,
	@SerializedName("password") val password: String,
	@SerializedName("fcmToken") val fcmToken: String?
)