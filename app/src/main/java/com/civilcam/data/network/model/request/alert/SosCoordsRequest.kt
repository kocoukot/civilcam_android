package com.civilcam.data.network.model.request.alert

import com.google.gson.annotations.SerializedName

class SosCoordsRequest(
    @SerializedName("location") private val location: String,
    @SerializedName("coords") private val coords: CoordsRequest,
)