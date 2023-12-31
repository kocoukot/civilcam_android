package com.civilcam.domainLayer.usecase.user

import com.civilcam.domainLayer.repos.UserRepository

class CheckCurrentPasswordUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(password: String) = userRepository.checkPassword(password)
}