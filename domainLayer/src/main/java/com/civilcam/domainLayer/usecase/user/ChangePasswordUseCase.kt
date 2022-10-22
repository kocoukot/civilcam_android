package com.civilcam.domainLayer.usecase.user

import com.civilcam.domainLayer.repos.UserRepository

class ChangePasswordUseCase(
	private val userRepository: UserRepository
) {
	suspend operator fun invoke(currentPassword: String, newPassword: String) =
        userRepository.changePassword(currentPassword, newPassword)
}