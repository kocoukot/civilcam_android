package com.civilcam.service

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.civilcam.R
import com.civilcam.domainLayer.model.user.NotificationType
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class FileDownloadWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val fileUrl = inputData.getString(KEY_FILE_URL) ?: ""
        val fileName = inputData.getString(KEY_FILE_NAME) ?: ""

        val builder = NotificationCompat.Builder(context, NotificationType.DOWNLOAD.notifyName)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Downloading your file...")
            .setOngoing(true)
            .setProgress(0, 0, true)

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
        var uri: Uri? = null
        var error = ""
        try {

            uri = getSavedFileUri(
                fileName = fileName,
                fileUrl = fileUrl,
                context = context
            )
        } catch (e: Throwable) {
            error = e.localizedMessage.orEmpty()
        }

        NotificationManagerCompat.from(context).cancel(NOTIFICATION_ID)
        Timber.tag("video").i("")
        return if (uri != null) {
            Result.success()
        } else {
            Result.failure(workDataOf(KEY_LOADING_ERROR to error))
        }
    }

    private fun getSavedFileUri(
        fileName: String,
        fileUrl: String,
        context: Context
    ): Uri? {
        val mimeType = "video/mp4"
        if (mimeType.isEmpty()) return null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                put(MediaStore.MediaColumns.RELATIVE_PATH, DOWNLOAD_FOLDER)
            }

            val resolver = context.contentResolver

            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

            return if (uri != null) {
                URL(fileUrl).openStream().use { input ->
                    resolver.openOutputStream(uri).use { output ->
                        input.copyTo(output!!, DEFAULT_BUFFER_SIZE)
                    }
                }
                uri
            } else {
                null
            }

        } else {

            val target = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                fileName
            )
            URL(fileUrl).openStream().use { input ->
                FileOutputStream(target).use { output ->
                    input.copyTo(output)
                }
            }

            return target.toUri()
        }
    }


    companion object {
        const val KEY_FILE_URL = "key_file_url"
        const val KEY_FILE_NAME = "key_file_name"
        const val KEY_FILE_URI = "key_file_uri"
        const val WORK_MANAGER_ID = "one_time_work"
        const val KEY_LOADING_ERROR = "key_loading_error"

        const val NOTIFICATION_ID = 333
        const val DOWNLOAD_FOLDER = "Download/Civilcam"
    }
}