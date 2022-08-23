package com.civilcam.data.network.model.response.auth

import com.google.gson.annotations.SerializedName

class RecoveryTokenResponse(
	@SerializedName("recoveryToken") val recoveryToken: String
)