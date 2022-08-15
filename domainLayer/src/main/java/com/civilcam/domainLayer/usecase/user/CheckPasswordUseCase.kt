package com.civilcam.domainLayer.usecase.user

import com.civilcam.domainLayer.repos.UserRepository

class CheckPasswordUseCase(
	private val userRepository: UserRepository
) {
	suspend fun checkPassword(password: String) = userRepository.checkPassword(password)
}