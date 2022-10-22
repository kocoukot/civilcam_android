package com.civilcam.domainLayer.usecase.user

import com.civilcam.domainLayer.repos.UserRepository

class GetCurrentUserUseCase(private val userRepository: UserRepository) {
	suspend fun invoke() = userRepository.getCurrentUser()
}