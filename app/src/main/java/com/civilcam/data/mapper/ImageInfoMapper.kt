package com.civilcam.data.mapper

import com.civilcam.data.network.model.response.ImageInfoResponse

class ImageInfoMapper : Mapper<ImageInfoResponse, com.civilcam.domainLayer.model.ImageInfo>(
    fromData = { response ->
        response.let {
            com.civilcam.domainLayer.model.ImageInfo(
                mimetype = it.mimetype,
                imageUrl = it.imageUrl,
                imageKey = it.imageKey,
                thumbnailKey = it.thumbnailKey,
                thumbnailUrl = it.thumbnailUrl,
            )
        }
    }
)