package com.civilcam.domainLayer.usecase.user

import com.civilcam.domainLayer.repos.UserRepository

class AcceptLegalDocsUseCase(
    private val userRepository: UserRepository
) {
    suspend fun invoke() = userRepository.acceptTerms()
}