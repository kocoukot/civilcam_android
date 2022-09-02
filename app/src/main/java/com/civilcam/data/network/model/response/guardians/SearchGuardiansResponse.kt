package com.civilcam.data.network.model.response.guardians

import com.google.gson.annotations.SerializedName

class SearchGuardiansResponse(
    @SerializedName("list") val list: List<PersonResponse>
)