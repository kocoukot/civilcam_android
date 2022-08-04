package com.civilcam.domain.usecase.auth

import com.civilcam.data.repository.AccountRepository
import com.civilcam.data.repository.AuthRepository

class SingUpUseCase(
    private val authRepository: AuthRepository,
    private val accountRepository: AccountRepository

) {
    suspend fun invoke(email: String, password: String) = authRepository.signUp(email, password)
        .also { accountRepository.loginUser(it.accessToken.orEmpty(), it) }
}