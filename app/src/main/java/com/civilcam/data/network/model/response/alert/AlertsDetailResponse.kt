package com.civilcam.data.network.model.response.alert

import com.civilcam.data.network.model.response.guardians.PersonResponse
import com.google.gson.annotations.SerializedName

class AlertsDetailResponse(
    @SerializedName("alert") val alert: AlertPersonResponse,
    @SerializedName("resolvers") val resolvers: List<ResolverResponse>,
) {
    class ResolverResponse(
        @SerializedName("id") val id: Int,
        @SerializedName("date") val date: String,
        @SerializedName("person") val person: PersonResponse,
    )
}