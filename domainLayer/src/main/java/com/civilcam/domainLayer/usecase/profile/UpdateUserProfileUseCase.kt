package com.civilcam.domainLayer.usecase.profile

import com.civilcam.domainLayer.model.UserSetupModel
import com.civilcam.domainLayer.repos.ProfileRepository

class UpdateUserProfileUseCase(
	private val profileRepository: ProfileRepository
) {
	suspend fun invoke(profile: UserSetupModel) = profileRepository.updateUserProfile(profile)
}