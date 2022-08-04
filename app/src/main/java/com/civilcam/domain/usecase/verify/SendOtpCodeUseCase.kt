package com.civilcam.domain.usecase.verify

import com.civilcam.data.repository.VerificationRepository
import com.civilcam.domain.model.VerificationFlow

class SendOtpCodeUseCase(private val verificationRepository: VerificationRepository) {
    suspend fun invoke(verificationFlow: VerificationFlow) =
        verificationRepository.resendOtpCode(verificationFlow)
}