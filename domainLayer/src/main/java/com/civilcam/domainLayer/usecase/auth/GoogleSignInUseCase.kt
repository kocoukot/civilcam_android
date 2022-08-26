package com.civilcam.domainLayer.usecase.auth

import com.civilcam.domainLayer.repos.AccountRepository
import com.civilcam.domainLayer.repos.AuthRepository

class GoogleSignInUseCase(
    private val authRepository: AuthRepository,
    private val accountRepository: AccountRepository
) {

    suspend fun invoke(accessToken: String) = authRepository.googleSignIn(accessToken)
        .also { accountRepository.loginUser(it.accessToken.orEmpty(), it) }
}
