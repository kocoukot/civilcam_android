package com.civilcam.domainLayer.usecase.verify

import com.civilcam.domainLayer.model.VerificationFlow
import com.civilcam.domainLayer.repos.VerificationRepository

class SendOtpCodeUseCase(private val verificationRepository: VerificationRepository) {
    suspend fun invoke(verificationFlow: VerificationFlow) =
        verificationRepository.resendOtpCode(verificationFlow)
}