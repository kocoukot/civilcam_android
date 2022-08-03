package com.civilcam.domain.usecase.auth

import com.civilcam.data.repository.AuthRepository

class SingUpUseCase(
    private val authRepository: AuthRepository
) {
    suspend fun invoke(email: String, password: String) = authRepository.signUp(email, password)
}