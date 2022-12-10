package com.civilcam.domainLayer.repos

interface VideoRepository {

    fun downloadVideo(videoUrl: String, videoName: String, videoId: Int)

    suspend fun checkIfExists(videoName: String): Boolean
}
