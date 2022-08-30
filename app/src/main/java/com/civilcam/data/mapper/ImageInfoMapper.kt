package com.civilcam.data.mapper

import com.civilcam.data.network.model.response.ImageInfoResponse
import com.civilcam.domainLayer.model.user.ImageInfo

class ImageInfoMapper : Mapper<ImageInfoResponse, ImageInfo>(
    fromData = { response ->
        response.let {
            ImageInfo(
                imageKey = it.imageKey,
                imageUrl = it.imageUrl,
                mimetype = it.imageMimetype,
                thumbnailKey = it.thumbnailKey,
                thumbnailUrl = it.thumbnailUrl,
            )
        }
    }
)