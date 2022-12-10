package com.civilcam.data.repository

import android.os.Build
import android.os.Environment
import androidx.work.*
import com.civilcam.CivilcamApplication.Companion.instance
import com.civilcam.domainLayer.repos.VideoRepository
import com.civilcam.service.FileDownloadWorker
import com.civilcam.service.FileDownloadWorker.Companion.DOWNLOAD_FOLDER
import com.civilcam.service.FileDownloadWorker.Companion.WORK_MANAGER_ID
import timber.log.Timber
import java.io.File

class VideoRepositoryImpl : VideoRepository {

    override fun downloadVideo(videoUrl: String, videoName: String, videoId: Int) {
        val data = Data.Builder()

        data.apply {
            putString(FileDownloadWorker.KEY_FILE_NAME, videoName)
            putString(FileDownloadWorker.KEY_FILE_URL, videoUrl)
        }

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .setRequiresBatteryNotLow(true)
            .build()

        val fileDownloadWorker = OneTimeWorkRequestBuilder<FileDownloadWorker>()
            .setConstraints(constraints)
            .setInputData(data.build())
            .build()

        WorkManager.getInstance(instance).enqueueUniqueWork(
            WORK_MANAGER_ID + videoId,
            ExistingWorkPolicy.KEEP,
            fileDownloadWorker
        )
    }

    override suspend fun checkIfExists(videoName: String): Boolean {
        val isVideoExists = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val file1 =
                File("${Environment.getExternalStorageDirectory().path}/$DOWNLOAD_FOLDER/$videoName")
            Timber.tag("video").i("file1 $file1 videoName $videoName")
            file1.isFile
        } else {
            val file2 = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "$videoName"
            )
            Timber.tag("video").i("file2 $file2")

            file2.isFile
        }
        Timber.tag("video").i("isVideoExists $isVideoExists")
        return isVideoExists
    }
}