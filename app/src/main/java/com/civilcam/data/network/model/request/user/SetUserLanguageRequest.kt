package com.civilcam.data.network.model.request.user

import com.civilcam.domainLayer.model.LanguageType
import com.google.gson.annotations.SerializedName

class SetUserLanguageRequest(
	@SerializedName("language") val language: LanguageType
)