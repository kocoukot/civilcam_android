package com.civilcam.domainLayer.repos

import android.net.Uri
import java.io.File

interface VideoRepository {

    fun downloadVideo(videoUrl: String, videoName: String, videoId: Int)

    suspend fun checkIfExists(videoName: String): Boolean

    suspend fun getVideoUri(videoName: String): Uri

    fun getFile(videoName: String): File
}
