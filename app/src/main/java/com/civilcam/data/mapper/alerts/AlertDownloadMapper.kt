package com.civilcam.data.mapper.alerts

import com.civilcam.data.mapper.Mapper
import com.civilcam.data.network.model.response.alert.AlertInfoResponse
import com.civilcam.domainLayer.model.alerts.AlertInfoModel

class AlertDownloadMapper :
	Mapper<AlertInfoResponse.DownloadResponse, AlertInfoModel.DownloadInfo>(fromData = {
		AlertInfoModel.DownloadInfo(
			it.id, it.quality, it.fileName, it.duration, it.url
		)
	})