package com.civilcam.domainLayer.model.alerts

data class AlertInfoModel(
	val alertModel: AlertModel,
	val alertResolvers: List<AlertDetailModel.AlertResolver>,
	val alertDownloads: List<DownloadInfo>
) {
	
	data class DownloadInfo(
		val id: Int,
		val quality: String?,
		val fileName: String,
		val duration: Int,
		val url: String?
	)
	
}