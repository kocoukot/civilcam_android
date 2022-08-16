package com.civilcam.domainLayer.usecase.auth

import com.civilcam.domainLayer.repos.AccountRepository
import com.civilcam.domainLayer.repos.AuthRepository

class SignInUseCase(
	private val authRepository: AuthRepository,
	private val accountRepository: AccountRepository

) {
	suspend fun invoke(email: String, password: String) = authRepository.signIn(email, password)
		.also { accountRepository.loginUser(it.accessToken.orEmpty(), it) }
}