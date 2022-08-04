package com.civilcam.data.repository

import com.civilcam.domain.model.CurrentUser
import com.civilcam.domain.model.VerificationFlow

interface VerificationRepository {

    suspend fun verifyOtpCode(verifyFlow: VerificationFlow, code: String): CurrentUser

    suspend fun resendOtpCode(otpType: VerificationFlow): Long
}