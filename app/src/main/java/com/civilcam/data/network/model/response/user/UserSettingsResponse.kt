package com.civilcam.data.network.model.response.user

import com.google.gson.annotations.SerializedName

class UserSettingsResponse(
    @SerializedName("isEmailAlertOn") val isEmailAlertOn: Boolean,
    @SerializedName("isSmsAlertOn") val isSmsAlertOn: Boolean,
    @SerializedName("isFaceIdOn") val isFaceIdOn: Boolean
)