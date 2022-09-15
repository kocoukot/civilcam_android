package com.civilcam.data.network.model.response.alert

import com.civilcam.data.network.model.response.guardians.PersonResponse
import com.google.gson.annotations.SerializedName

class AlertPersonResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("date") val date: String,
    @SerializedName("location") val location: String,
    @SerializedName("person") val person: PersonResponse,
    @SerializedName("url") val url: String,
    @SerializedName("status") val status: String,
)