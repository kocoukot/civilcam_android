package com.civilcam.domainLayer.usecase.user

import com.civilcam.domainLayer.repos.UserRepository

class LogoutUseCase(
    private val userRepository: UserRepository
) {
    suspend fun logout() = userRepository.signOut()
}