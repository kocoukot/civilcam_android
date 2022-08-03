package com.civilcam.data.network.model.response

import com.google.gson.annotations.SerializedName

class ErrorResponse(
    @SerializedName("errorCode") val errorCode: Int,
    @SerializedName("title") val title: String,
    @SerializedName("message") val message: String,
    @SerializedName("isForceLogout") val isForceLogout: Boolean

)
