package com.civilcam.domain.usecase.profile

import com.civilcam.data.repository.ProfileRepository
import com.civilcam.domain.model.UserSetupModel

class SetPersonalInfoUseCase(
    private val profileRepository: ProfileRepository
) {
    suspend fun invoke(profile: UserSetupModel) = profileRepository.setUserProfile(profile)
}