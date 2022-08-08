package com.civilcam.domainLayer.usecase.profile

import com.civilcam.domainLayer.repos.ProfileRepository

class ChangePhoneNumberUseCase(private val profileRepository: ProfileRepository) {
	suspend fun invoke(phone: String) = profileRepository.editPhoneNumber(phone)
}