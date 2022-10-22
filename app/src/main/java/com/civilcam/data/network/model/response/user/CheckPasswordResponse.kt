package com.civilcam.data.network.model.response.user

import com.google.gson.annotations.SerializedName

class CheckPasswordResponse(
	@SerializedName("isMatch") val isMatch: Boolean
)