package com.civilcam.domainLayer.model.alerts

import com.civilcam.domainLayer.model.guard.PersonModel

data class AlertDetailModel(
    val alertModel: AlertModel,
    val alertResolvers: List<AlertResolver>,
    val alertDownloads: List<DownloadInfo>,
    val updateUI: Int = 0
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
        var videoState: VideoLoadingState = VideoLoadingState.ReadyToLoad
    )

    fun getNameForVideo(): String = "${
        alertModel.userInfo?.personFullName?.replace(
            " ",
            "_"
        )
    }_${alertModel.alertDate.replace(":", "_")}.mp4"


}