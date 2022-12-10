package com.civilcam.domainLayer.model.alerts

import com.civilcam.domainLayer.model.guard.PersonModel

data class AlertDetailModel(
    val alertModel: AlertModel,
    val alertResolvers: List<AlertResolver>,
    val alertDownloads: List<DownloadInfo>
) {
    class AlertResolver(
        val resolveId: Int,
        val resolveDate: String,
        val userInfo: PersonModel,
    )

    data class DownloadInfo(
        val id: Int,
        val quality: String?,
        val fileName: String,
        val duration: Int,
        val url: String?,
        val videoState: VideoLoadingState = VideoLoadingState.ReadyToLoad
    )

}