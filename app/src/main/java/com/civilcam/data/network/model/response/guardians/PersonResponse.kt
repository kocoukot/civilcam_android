package com.civilcam.data.network.model.response.guardians

import com.civilcam.data.network.model.response.ImageInfoResponse
import com.civilcam.domainLayer.model.user.LanguageType
import com.google.gson.annotations.SerializedName

class PersonResponse(
    @SerializedName("id") val id: String,
    @SerializedName("language") val language: LanguageType,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("dob") val dob: String,
    @SerializedName("avatar") val avatar: ImageInfoResponse?,
    @SerializedName("phone") val phone: String,
    @SerializedName("address") val address: String,

    @SerializedName("isOnGuard") val isOnGuard: Boolean?,
    @SerializedName("isGuardian") val isGuardian: Boolean?,
    @SerializedName("request") val request: RequestModelResponse?,
) {

    class RequestModelResponse(
        @SerializedName("id") val id: Int,
        @SerializedName("status") val status: String
    )
}