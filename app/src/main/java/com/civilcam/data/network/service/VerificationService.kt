package com.civilcam.data.network.service

import com.civilcam.data.network.Endpoint
import com.civilcam.data.network.model.request.verify.OTPTypeRequest
import com.civilcam.data.network.model.request.verify.VerifyCodeRequest
import com.civilcam.data.network.model.response.IsSentResponse
import com.civilcam.data.network.model.response.auth.UserResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface VerificationService {

    @POST(Endpoint.Verification.VERIFY_OTP_CODE)
    suspend fun verifyOtpCode(@Body request: VerifyCodeRequest): UserResponse

    @POST(Endpoint.Verification.RESEND_OTP_CODE)
    suspend fun resendOtpCode(@Body request: OTPTypeRequest): IsSentResponse
}


