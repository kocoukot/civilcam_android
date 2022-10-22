package com.civilcam.data.network.model.request.alert

import com.google.gson.annotations.SerializedName

class AlertDetailRequest(
    @SerializedName("id") private val id: Int,
)