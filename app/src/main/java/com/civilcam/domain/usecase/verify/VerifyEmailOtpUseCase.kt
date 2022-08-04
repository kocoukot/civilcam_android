package com.civilcam.domain.usecase.verify

import com.civilcam.data.repository.VerificationRepository
import com.civilcam.domain.model.VerificationFlow

class VerifyEmailOtpUseCase(
    private val verificationRepository: VerificationRepository
) {
    suspend fun verifyOtp(verificationFlow: VerificationFlow, otpCode: String) =
        verificationRepository.verifyOtpCode(verificationFlow, otpCode)
}