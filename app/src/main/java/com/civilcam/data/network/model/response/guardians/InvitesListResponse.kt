package com.civilcam.data.network.model.response.guardians

import com.google.gson.annotations.SerializedName

class InvitesListResponse(
    @SerializedName("list") val list: List<InviteResponse>
) {
    class InviteResponse(
        @SerializedName("id") val id: Int,
        @SerializedName("phone") val phone: String,
        @SerializedName("status") val status: String,
    )
}