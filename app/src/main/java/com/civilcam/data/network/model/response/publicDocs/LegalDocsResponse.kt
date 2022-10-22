package com.civilcam.data.network.model.response.publicDocs

import com.google.gson.annotations.SerializedName

class LegalDocsResponse(
    @SerializedName("termsAndConditions") val termsAndConditions: String,
    @SerializedName("privacyPolicy") val privacyPolicy: String,
)