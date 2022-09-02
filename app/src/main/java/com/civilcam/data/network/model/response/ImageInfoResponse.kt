package com.civilcam.data.network.model.response

import com.google.gson.annotations.SerializedName

class ImageInfoResponse(
    @SerializedName("mimetype") val imageMimetype: String? = "",
    @SerializedName("imageUrl") val imageUrl: String? = "",
    @SerializedName("imageKey") val imageKey: String? = "",
    @SerializedName("thumbnailKey") val thumbnailKey: String? = "",
    @SerializedName("thumbnailUrl") val thumbnailUrl: String? = "",
)