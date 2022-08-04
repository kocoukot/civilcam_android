package com.civilcam.data.mapper

import com.civilcam.data.network.model.response.ImageInfoResponse
import com.civilcam.domain.model.ImageInfo

class ImageInfoMapper : Mapper<ImageInfoResponse, ImageInfo>(
    fromData = { response ->
        response.let {
            ImageInfo(
                mimetype = it.mimetype,
                imageUrl = it.imageUrl,
                imageKey = it.imageKey,
                thumbnailKey = it.thumbnailKey,
                thumbnailUrl = it.thumbnailUrl,
            )
        }
    }
)