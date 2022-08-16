package com.civilcam.domainLayer.usecase.user

import com.civilcam.domainLayer.repos.UserRepository

class ChangeEmailUseCase(
	private val userRepository: UserRepository
) {
	suspend fun changeEmail(currentEmail: String, newEmail: String) =
		userRepository.changeEmail(currentEmail, newEmail)
}