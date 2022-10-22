package com.civilcam.data.network.model.request.guardians

import com.google.gson.annotations.SerializedName

class InviteByPhoneRequest(
    @SerializedName("phone") private val phone: String
)