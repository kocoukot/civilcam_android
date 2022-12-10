package com.civilcam.data.mapper.alerts

import com.civilcam.data.mapper.Mapper
import com.civilcam.data.network.model.response.alert.AlertInfoResponse
import com.civilcam.domainLayer.model.alerts.AlertDetailModel

class AlertDownloadMapper :
    Mapper<AlertInfoResponse.DownloadResponse, AlertDetailModel.DownloadInfo>(fromData = {
        AlertDetailModel.DownloadInfo(
            id = it.id,
            quality = it.quality,
            fileName = it.fileName,
            duration = it.duration,
            url = it.url

        )
    })