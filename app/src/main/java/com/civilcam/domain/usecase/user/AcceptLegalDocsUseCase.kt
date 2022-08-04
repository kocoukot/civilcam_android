package com.civilcam.domain.usecase.user

import com.civilcam.data.repository.UserRepository

class AcceptLegalDocsUseCase(
    private val userRepository: UserRepository
) {
    suspend fun invoke() = userRepository.acceptTerms()
}