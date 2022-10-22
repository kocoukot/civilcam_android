package com.civilcam.data.network.model.request.alert

import com.civilcam.domainLayer.model.PaginationRequest
import com.google.gson.annotations.SerializedName

class AlertHistoryRequest(
    @SerializedName("pageInfo") private val pageInfo: PaginationRequest.Pagination,
    @SerializedName("alertsType") private val alertsType: String,
)