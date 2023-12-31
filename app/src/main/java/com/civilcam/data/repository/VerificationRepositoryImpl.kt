package com.civilcam.data.repository

import com.civilcam.data.local.AccountStorage
import com.civilcam.data.mapper.auth.UserMapper
import com.civilcam.data.network.model.request.verify.OTPTypeRequest
import com.civilcam.data.network.model.request.verify.VerifyCodeRequest
import com.civilcam.data.network.service.VerificationService
import com.civilcam.data.network.support.BaseRepository
import com.civilcam.data.network.support.Resource
import com.civilcam.domainLayer.model.VerificationFlow
import com.civilcam.domainLayer.model.user.CurrentUser
import com.civilcam.domainLayer.repos.VerificationRepository

class VerificationRepositoryImpl(
    private val verificationService: VerificationService,
    private val accountStorage: AccountStorage,
) : VerificationRepository, BaseRepository() {

    private val sessionUserMapper = UserMapper()

    override suspend fun verifyOtpCode(verifyFlow: VerificationFlow, code: String): CurrentUser =
        safeApiCall {
            verificationService.verifyOtpCode(
                VerifyCodeRequest(otpType = verifyFlow.rawValue, code = code)
            )
        }.let { response ->
            when (response) {
                is Resource.Success -> sessionUserMapper.mapData(response.value)
                is Resource.Failure -> {
                    response.checkIfLogOut { accountStorage.logOut() }
                    throw response.serviceException
                }
            }
        }

    override suspend fun resendOtpCode(otpType: VerificationFlow): Long =
        safeApiCall {
            verificationService.resendOtpCode(
                OTPTypeRequest(otpType.rawValue)
            )
        }.let { response ->
            when (response) {
                is Resource.Success -> response.value.timeout.toLong()
                is Resource.Failure -> {
                    response.checkIfLogOut { accountStorage.logOut() }
                    throw response.serviceException
                }
            }
        }
}