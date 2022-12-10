package com.civilcam.domainLayer.usecase.alerts

import com.civilcam.domainLayer.repos.VideoRepository

class GetVideoUriUseCase(
    private val videoRepository: VideoRepository
) {
    suspend operator fun invoke(videoName: String) = videoRepository.getVideoUri(videoName)
}