package com.civilcam.domainLayer.usecase.auth

import com.civilcam.domainLayer.repos.AuthRepository

class RecoverPasswordUseCase(
	private val authRepository: AuthRepository
) {
	suspend fun recoverPassword(token: String, password: String) =
		authRepository.recoverPassword(token, password)
}