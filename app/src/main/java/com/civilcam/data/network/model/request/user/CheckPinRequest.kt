package com.civilcam.data.network.model.request.user

import com.google.gson.annotations.SerializedName

class CheckPinRequest(
	@SerializedName("pinCode") private val pinCode: String
)