package com.civilcam.domain.usecase.profile

import com.civilcam.data.repository.ProfileRepository

class ChangePhoneNumberUseCase(private val profileRepository: ProfileRepository) {
	suspend fun invoke(phone: String) = profileRepository.editPhoneNumber(phone)
}