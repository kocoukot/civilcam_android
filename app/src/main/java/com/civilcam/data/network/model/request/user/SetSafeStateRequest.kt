package com.civilcam.data.network.model.request.user

import com.civilcam.data.network.model.request.alert.CoordsRequest
import com.google.gson.annotations.SerializedName

class SetSafeStateRequest(
    @SerializedName("coords") private val coords: CoordsRequest,
    @SerializedName("pinCode") private val pinCode: String,
)