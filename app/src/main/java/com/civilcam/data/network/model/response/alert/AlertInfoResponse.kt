package com.civilcam.data.network.model.response.alert

import com.google.gson.annotations.SerializedName

class AlertInfoResponse(
	@SerializedName("alert") val alert: AlertPersonResponse,
	@SerializedName("resolvers") val resolvers: List<AlertsDetailResponse.ResolverResponse>,
	@SerializedName("downloads") val downloads: List<DownloadResponse>
) {
	
	class DownloadResponse(
		@SerializedName("id") val id: Int,
		@SerializedName("quality") val quality: String?,
		@SerializedName("fileName") val fileName: String,
		@SerializedName("fileSize") val fileSize: Int,
		@SerializedName("duration") val duration: Int,
		@SerializedName("url") val url: String?
	)
}