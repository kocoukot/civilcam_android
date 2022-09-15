package com.civilcam.data.network.model.request.alert

import com.google.gson.annotations.SerializedName

class CoordsRequest(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
)