package com.civilcam.data.repository

import com.civilcam.common.ext.BaseRepository
import com.civilcam.common.ext.Resource
import com.civilcam.data.network.model.request.verify.OTPTypeRequest
import com.civilcam.data.network.service.VerificationService
import com.civilcam.domain.model.VerificationFlow

class VerificationRepositoryImpl(
    private val verificationService: VerificationService
) : VerificationRepository, BaseRepository() {

//    private val sessionUserMapper = UserMapper()

//    override suspend fun verifyOtpCode(verifyFlow: VerificationFlow, code: String): CurrentUser =
//        safeApiCall {
//            verificationService.verifyOtpCode(
//                VerifyCodeRequest(
//                    otpType = verifyFlow.rawValue,
//                    code = code
//                )
//            )
//        }.let { response ->
//            Timber.d("response $response")
//            when (response) {
//                is Resource.Success -> {
//                    sessionUserMapper.mapData(response.value)
//                }
//                is Resource.Failure -> {
//                    throw response.serviceException
//                }
//            }
//        }

    override suspend fun resendOtpCode(otpType: VerificationFlow): Long =
        safeApiCall {
            verificationService.resendOtpCode(
                OTPTypeRequest(otpType.rawValue)
            )
        }.let { response ->
            when (response) {
                is Resource.Success -> {
                    response.value.timeout.toLong()
                }
                is Resource.Failure -> {
                    throw response.serviceException
                }
            }
        }
}