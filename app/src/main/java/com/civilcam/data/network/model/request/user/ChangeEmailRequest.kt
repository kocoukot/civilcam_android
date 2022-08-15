package com.civilcam.data.network.model.request.user

import com.google.gson.annotations.SerializedName

class ChangeEmailRequest(
	@SerializedName("currentEmail") val currentEmail: String,
	@SerializedName("newEmail") val newEmail: String
)