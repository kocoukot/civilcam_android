package com.civilcam.data.network.model.response.alert

import com.civilcam.data.network.model.response.guardians.PersonResponse
import com.civilcam.domainLayer.model.StreamStatusType
import com.google.gson.annotations.SerializedName

class AlertPersonResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("date") val date: String,
    @SerializedName("location") val location: String,
    @SerializedName("person") val person: PersonResponse?,
    @SerializedName("url") val url: String?,
    @SerializedName("key") val key: String?,
    @SerializedName("status") val status: String,
    @SerializedName("streamStatus") val streamStatus: String?
)