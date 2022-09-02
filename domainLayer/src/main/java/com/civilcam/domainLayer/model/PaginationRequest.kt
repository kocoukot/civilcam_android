package com.civilcam.domainLayer.model

import com.google.gson.annotations.SerializedName

class PaginationRequest(
    @SerializedName("pageInfo") val pageInfo: Pagination = Pagination(),
) {
    class Pagination(
        @SerializedName("pageIndex") val pageIndex: Int = 0, // min 1
        @SerializedName("itemsPerPage") val itemsPerPage: Int = 40,
    )
}