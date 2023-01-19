package com.civilcam.data.network.model.request.guardians

import com.google.gson.annotations.SerializedName

class MatchUsersByPhoneRequest(
    @SerializedName("phones") private val phones: List<String>
)