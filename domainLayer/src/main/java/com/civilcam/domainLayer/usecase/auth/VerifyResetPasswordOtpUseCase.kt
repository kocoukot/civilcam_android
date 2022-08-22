package com.civilcam.domainLayer.usecase.auth

import com.civilcam.domainLayer.repos.AuthRepository

class VerifyResetPasswordOtpUseCase(
	private val authRepository: AuthRepository
) {
	suspend fun verifyOtp(email: String, code: String) =
		authRepository.verifyResetPasswordOtpCode(email, code)
}