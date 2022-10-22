package com.civilcam.data.network.model.response.guardians

import com.google.gson.annotations.SerializedName

class PersonDetailResponse(
    @SerializedName("person") val person: PersonResponse,
)