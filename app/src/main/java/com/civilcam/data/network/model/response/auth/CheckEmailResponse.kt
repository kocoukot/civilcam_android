package com.civilcam.data.network.model.response.auth

import com.google.gson.annotations.SerializedName

class CheckEmailResponse(
	@SerializedName("exists") val exists: Boolean
)