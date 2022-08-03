package com.civilcam.data.network.model.response

import com.google.gson.annotations.SerializedName

class IsSentResponse(
    @SerializedName("isSent") val isSent: Boolean,
    @SerializedName("timeout") val timeout: Int,
)