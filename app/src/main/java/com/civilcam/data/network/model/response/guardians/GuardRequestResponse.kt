package com.civilcam.data.network.model.response.guardians

import com.google.gson.annotations.SerializedName

class GuardRequestResponse(
    @SerializedName("request") private val request: PersonResponse.RequestModelResponse
)