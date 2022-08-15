package com.civilcam.data.network.model.request.user

import com.google.gson.annotations.SerializedName

class CheckPasswordRequest(
	@SerializedName("password") val password: String
)