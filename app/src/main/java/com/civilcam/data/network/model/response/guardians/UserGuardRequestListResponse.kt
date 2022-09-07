package com.civilcam.data.network.model.response.guardians

import com.google.gson.annotations.SerializedName

class UserGuardRequestListResponse(
    @SerializedName("list") val list: List<UserNetworkResponse.NetworkRequestsResponse>,
)