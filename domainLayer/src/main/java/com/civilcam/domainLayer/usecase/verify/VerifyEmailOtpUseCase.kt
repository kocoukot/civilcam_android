package com.civilcam.domainLayer.usecase.verify

import com.civilcam.domainLayer.model.VerificationFlow
import com.civilcam.domainLayer.repos.VerificationRepository

class VerifyEmailOtpUseCase(
    private val verificationRepository: VerificationRepository
) {
    suspend fun verifyOtp(verificationFlow: VerificationFlow, otpCode: String) =
        verificationRepository.verifyOtpCode(verificationFlow, otpCode)
}