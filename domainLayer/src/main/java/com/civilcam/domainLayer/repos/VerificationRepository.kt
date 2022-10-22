package com.civilcam.domainLayer.repos

import com.civilcam.domainLayer.model.VerificationFlow
import com.civilcam.domainLayer.model.user.CurrentUser

interface VerificationRepository {

    suspend fun verifyOtpCode(verifyFlow: VerificationFlow, code: String): CurrentUser

    suspend fun resendOtpCode(otpType: VerificationFlow): Long
}