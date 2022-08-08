package com.civilcam.domainLayer.repos

import com.civilcam.domainLayer.model.CurrentUser
import com.civilcam.domainLayer.model.VerificationFlow

interface VerificationRepository {

    suspend fun verifyOtpCode(verifyFlow: VerificationFlow, code: String): CurrentUser

    suspend fun resendOtpCode(otpType: VerificationFlow): Long
}