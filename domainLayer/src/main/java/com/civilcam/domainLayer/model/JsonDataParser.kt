package com.civilcam.domainLayer.model

import com.google.gson.annotations.SerializedName


data class JsonDataParser(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
)