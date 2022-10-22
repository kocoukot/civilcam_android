package com.civilcam.data.network.model.request.guardians

import com.civilcam.domainLayer.model.PaginationRequest
import com.google.gson.annotations.SerializedName

class SearchGuardiansRequest(
    @SerializedName("pageInfo") private val pageInfo: PaginationRequest.Pagination,
    @SerializedName("search") private val search: String
)