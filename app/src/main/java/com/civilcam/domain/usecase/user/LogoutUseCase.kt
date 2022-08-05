package com.civilcam.domain.usecase.user

import com.civilcam.data.repository.UserRepository

class LogoutUseCase(
    private val userRepository: UserRepository
) {
    suspend fun logout() = userRepository.signOut()
}