package com.civilcam.data.network.model.response.guardians

import com.civilcam.data.network.model.response.ImageInfoResponse
import com.civilcam.domainLayer.model.user.LanguageType
import com.google.gson.annotations.SerializedName

class SearchGuardiansResponse(
    @SerializedName("list") val list: List<SearchGuardianResponse>
) {

    class SearchGuardianResponse(
        @SerializedName("id") val id: Int,
        @SerializedName("language") val language: LanguageType,
        @SerializedName("fullName") val fullName: String,
        @SerializedName("avatar") val avatar: ImageInfoResponse?,
    )
}