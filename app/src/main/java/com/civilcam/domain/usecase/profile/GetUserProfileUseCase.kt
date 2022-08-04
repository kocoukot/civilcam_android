package com.civilcam.domain.usecase.profile

import com.civilcam.data.repository.ProfileRepository

class GetUserProfileUseCase(
    private val profileRepository: ProfileRepository
) {
    suspend fun invoke() = profileRepository.getUserProfile()
}