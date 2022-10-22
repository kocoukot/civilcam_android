package com.civilcam.data.network.model.request.user

import com.civilcam.domainLayer.model.user.LanguageType
import com.google.gson.annotations.SerializedName

class SetUserLanguageRequest(
    @SerializedName("language") private val language: LanguageType
)