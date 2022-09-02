package com.civilcam.data.network.model.response.guardians

import com.google.gson.annotations.SerializedName

class UserGuardRequestListResponse(
    @SerializedName("list") val list: List<GuardRequestResponse>,
) {

    class GuardRequestResponse(
        @SerializedName("id") val id: Int,
        @SerializedName("status") val status: String,
        @SerializedName("person") val person: PersonResponse
    )
}