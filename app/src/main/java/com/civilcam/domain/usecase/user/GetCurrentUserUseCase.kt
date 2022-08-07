package com.civilcam.domain.usecase.user

import com.civilcam.data.repository.UserRepository

class GetCurrentUserUseCase(private val userRepository: UserRepository) {
	suspend fun invoke() = userRepository.getCurrentUser()
}