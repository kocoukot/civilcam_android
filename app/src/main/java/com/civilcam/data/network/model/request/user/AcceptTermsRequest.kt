package com.civilcam.data.network.model.request.user

import com.google.gson.annotations.SerializedName

class AcceptTermsRequest(
    @SerializedName("setTermsPolicyAccepted") private val setTermsPolicyAccepted: Boolean,
)
