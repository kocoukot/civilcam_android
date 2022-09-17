package com.civilcam.domainLayer.usecase.user

import com.civilcam.domainLayer.repos.UserRepository

class SetSafeStateUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(pinCode: String) = userRepository.setSafeState(pinCode)
}