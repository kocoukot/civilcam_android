package com.civilcam.data.network.model.request.user

import com.google.gson.annotations.SerializedName

class SetPinRequest(
	@SerializedName("currentPinCode") private val currentPinCode: String?,
	@SerializedName("newPinCode") private val newPinCode: String
)