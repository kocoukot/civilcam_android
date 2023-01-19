package com.civilcam.data.network.model.response.guardians

import com.google.gson.annotations.SerializedName

class MatchUsersByPhoneResponse(
    @SerializedName("list") val list: List<MatchedUserResponse>
) {
    class MatchedUserResponse(
        @SerializedName("phone") val phone: String,
        @SerializedName("userId") val userId: Int,
        @SerializedName("status") val status: String?,
    )
}
