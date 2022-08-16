package com.civilcam.data.network.model.response

import com.google.gson.annotations.SerializedName

class SuccessResponse(
    @SerializedName("ok") val isOk: Boolean,
)