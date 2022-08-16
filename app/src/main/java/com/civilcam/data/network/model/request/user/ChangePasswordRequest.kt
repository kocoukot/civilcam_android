package com.civilcam.data.network.model.request.user

import com.google.gson.annotations.SerializedName

class ChangePasswordRequest(
	@SerializedName("currentPassword") val currentPassword: String,
	@SerializedName("newPassword") val newPassword: String
)