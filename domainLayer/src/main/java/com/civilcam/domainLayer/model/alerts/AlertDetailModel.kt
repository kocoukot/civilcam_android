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
        val fileSize: Int,
        val duration: Int,
        val url: String?,
        var videoState: VideoLoadingState = VideoLoadingState.ReadyToLoad
    ) {
        fun makeVideoName(namePart: String): String = "${namePart}_$fileName.mp4"

        fun getFileSize(): String {
            val value = String.format("%.2f", fileSize.toDouble() / (1024 * 1024))
            return "$value Mb"
        }
    }

    fun getNameForVideo(): String = "${
        alertModel.userInfo?.personFullName?.replace(
            " ",
            "_"
        )
    }_${alertModel.alertDate.replace(":", "_")}"


}