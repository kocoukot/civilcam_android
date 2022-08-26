package com.civilcam.data.network.model.request.user

import com.google.gson.annotations.SerializedName

class ContactSupportRequest(
    @SerializedName("issueName") private val issueName: String,
    @SerializedName("message") private val message: String,
    @SerializedName("replyTo") private val replyTo: String,
)