package com.civilcam.data.repository

import com.civilcam.common.ext.BaseRepository
import com.civilcam.common.ext.Resource
import com.civilcam.data.mapper.auth.UserMapper
import com.civilcam.data.network.model.request.verify.OTPTypeRequest
import com.civilcam.data.network.model.request.verify.VerifyCodeRequest
import com.civilcam.data.network.service.VerificationService

class VerificationRepositoryImpl(
    private val verificationService: VerificationService
) : com.civilcam.domainLayer.repos.VerificationRepository, BaseRepository() {

    private val sessionUserMapper = UserMapper()

    override suspend fun verifyOtpCode(verifyFlow: com.civilcam.domainLayer.model.VerificationFlow, code: String): com.civilcam.domainLayer.model.CurrentUser =
        safeApiCall {
            verificationService.verifyOtpCode(
                VerifyCodeRequest(
                    otpType = verifyFlow.rawValue,
                    code = code
                )
            )
        }.let { response ->
            when (response) {
                is Resource.Success -> sessionUserMapper.mapData(response.value)
                is Resource.Failure -> throw response.serviceException
            }
        }

    override suspend fun resendOtpCode(otpType: com.civilcam.domainLayer.model.VerificationFlow): Long =
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