package com.civilcam.domainLayer.usecase.alerts

import com.civilcam.domainLayer.repos.VideoRepository


class CheckIfVideoLoadedUseCase(
    private val videoRepository: VideoRepository
) {
    suspend operator fun invoke(videoName: String) = videoRepository.checkIfExists(videoName)
}