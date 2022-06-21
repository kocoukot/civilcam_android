package com.civilcam.data.local

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.civilcam.ui.common.Constant
import com.civilcam.utils.FileUtils
import com.standartmedia.data.local.model.ImageData
import com.standartmedia.data.local.model.ImageMetadata
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.runBlocking
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MediaStorage(private val context: Context) {
    private val imageCollection: Uri
        get() {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL_PRIMARY
                )
            } else {
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }
        }
    private val imageTitleFormatter =
        SimpleDateFormat("yyyyMMdd_hhmmss_SSS", Locale.US)

    fun createImageUri(): Maybe<Uri?> = Maybe
        .fromCallable {
            ContentValues()
                .apply {
                    Calendar.getInstance(TimeZone.getDefault()).timeInMillis
                        .also {
                            imageTitleFormatter.format(it).let { date ->
                                val title = "IMG_standartM_$date"
                                put(MediaStore.Images.Media.TITLE, title)
                                put(MediaStore.Images.Media.DISPLAY_NAME, title)
                            }
                            put(MediaStore.Images.Media.DATE_ADDED, it)
                            put(MediaStore.Images.Media.DATE_MODIFIED, it)
                        }
                    put(
                        MediaStore.Images.Media.MIME_TYPE,
                        "image/${Constant.DEFAULT_PICTURE_EXTENSION}"
                    )
                }
                .let {
                    context.contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        it
                    )
                }
        }
        .subscribeOn(Schedulers.io())

    fun getImageMetadata(uri: Uri): Maybe<ImageMetadata> = Maybe
        .fromCallable {
            val projection = arrayOf(
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE
            )
            context.contentResolver.query(uri, projection, null, null, null)
                ?.use { cursor ->
                    cursor
                        .takeIf { it.moveToNext() }
                        ?.let {
                            ImageMetadata(
                                uri = uri,
                                name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)),
                                sizeMb = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.SIZE))
                                    .toFloat() / 1024f / 1024f
                            )
                        }
                }

        }

    fun getImageFile(uri: Uri): Maybe<File> = getImageMetadata(uri)
        .flatMap { metadata ->
            Maybe.fromCallable {
                FileUtils.createFileFromUri(context, "standartM_${metadata.name}", uri)
                    ?.also {
                        runBlocking {
                            Compressor.compress(context, it) {
                                quality(Constant.IMAGE_COMPRESSION_QUALITY)
                            }
                        }
                    }
            }
        }

    fun getResourceFile(resourceName: String): Maybe<File> =
        Maybe.fromCallable {
            FileUtils.createFileFromResource(context, resourceName)
                ?.also {
                    runBlocking {
                        Compressor.compress(context, it) {
                            quality(Constant.IMAGE_COMPRESSION_QUALITY)
                        }
                    }
                }
        }

    fun getImageData(uri: Uri): Maybe<ImageData> = getImageFile(uri)
        .map { ImageData(it.name, it.extension, it.readBytes()) }
}