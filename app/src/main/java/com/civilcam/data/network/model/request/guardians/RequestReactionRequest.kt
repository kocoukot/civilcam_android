package com.civilcam.data.network.model.request.guardians

import com.google.gson.annotations.SerializedName

class RequestReactionRequest(
    @SerializedName("reaction") private val reaction: String,
    @SerializedName("id") private val id: Int,
)