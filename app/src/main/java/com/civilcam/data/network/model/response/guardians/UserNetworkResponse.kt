package com.civilcam.data.network.model.response.guardians

import com.google.gson.annotations.SerializedName

class UserNetworkResponse(
    @SerializedName("requests") val requests: List<NetworkRequestsResponse>,
    @SerializedName("onGuard") val onGuard: List<OnGuardResponse>,
    @SerializedName("guardians") val guardians: List<GuardiansResponse>,

    ) {

    class NetworkRequestsResponse(
        @SerializedName("id") val id: Int,
        @SerializedName("status") val status: String,
        @SerializedName("person") val person: PersonResponse,
    )

    class OnGuardResponse(
        @SerializedName("id") val id: String,
        @SerializedName("person") val person: PersonResponse,
        @SerializedName("status") val status: String,
    )

    class GuardiansResponse(
        @SerializedName("id") val id: String,
        @SerializedName("guardian") val guardian: PersonResponse,
        @SerializedName("status") val status: String,
    )
}