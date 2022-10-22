package com.civilcam.data.network.model.request.guardians

import com.google.gson.annotations.SerializedName

class PersonIdRequest(
    @SerializedName("personId") private val personId: Int,
)