package com.civilcam.domainLayer.usecase.auth

import com.civilcam.domainLayer.repos.AuthRepository

class ResetPasswordUseCase(
	private val authRepository: AuthRepository
) {
	suspend fun resetPassword(email: String) = authRepository.resetPassword(email)
}