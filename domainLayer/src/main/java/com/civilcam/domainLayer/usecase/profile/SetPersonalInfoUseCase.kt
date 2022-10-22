package com.civilcam.domainLayer.usecase.profile

import com.civilcam.domainLayer.model.profile.UserSetupModel
import com.civilcam.domainLayer.repos.ProfileRepository

class SetPersonalInfoUseCase(
    private val profileRepository: ProfileRepository
) {
    suspend fun invoke(profile: UserSetupModel) = profileRepository.setUserProfile(profile)
}