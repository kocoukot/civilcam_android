package com.civilcam.domainLayer.usecase.auth

import com.civilcam.domainLayer.repos.AuthRepository

class CheckEmailUseCase(
	private val authRepository: AuthRepository
) {
	suspend fun checkEmail(email: String) = authRepository.checkEmail(email)
}