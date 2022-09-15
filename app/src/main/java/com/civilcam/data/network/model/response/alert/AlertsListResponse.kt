package com.civilcam.data.network.model.response.alert

import com.google.gson.annotations.SerializedName

class AlertsListResponse(
    @SerializedName("list") val list: List<AlertPersonResponse>,
)