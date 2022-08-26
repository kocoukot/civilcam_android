package com.civilcam.data.network.model.request.user

import com.google.gson.annotations.SerializedName

class ToggleSettingsRequest(
    @SerializedName("type") private val type: String,
    @SerializedName("setOn") private val setOn: Boolean
)