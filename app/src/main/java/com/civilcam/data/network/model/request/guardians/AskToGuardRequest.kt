package com.civilcam.data.network.model.request.guardians

import com.google.gson.annotations.SerializedName

class AskToGuardRequest(
    @SerializedName("personId") private val personId: Int
)