package com.civilcam.domainLayer.usecase.profile

import com.civilcam.domainLayer.repos.ProfileRepository

class GetUserProfileUseCase(
    private val profileRepository: ProfileRepository
) {
    suspend fun invoke() = profileRepository.getUserProfile()
}