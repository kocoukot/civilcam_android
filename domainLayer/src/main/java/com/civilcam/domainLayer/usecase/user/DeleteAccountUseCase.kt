package com.civilcam.domainLayer.usecase.user

import com.civilcam.domainLayer.repos.UserRepository

class DeleteAccountUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() = userRepository.deleteAccount()
}