package com.civilcam.data.repository

import com.civilcam.common.ext.BaseRepository
import com.civilcam.common.ext.Resource
import com.civilcam.data.local.AccountStorage
import com.civilcam.data.local.SharedPreferencesStorage
import com.civilcam.data.network.model.request.user.AcceptTermsRequest
import com.civilcam.data.network.service.UserService

class UserRepositoryImpl(
    private val sharedPreferencesStorage: SharedPreferencesStorage,
    private val userService: UserService,
    private val accountStorage: AccountStorage
) : UserRepository, BaseRepository() {

    override suspend fun acceptTerms() =
        safeApiCall {
            userService.acceptTermsPolicy(AcceptTermsRequest(true))
        }.let { response ->
            when (response) {
                is Resource.Success -> response.value
                is Resource.Failure -> throw response.serviceException
            }
        }

//    override suspend fun signOut(): Boolean =
//        safeApiCall {
//            userService.signOut()
//        }.let { response ->
//            when (response) {
//                is Resource.Success -> {
//                    accountStorage.logOut()
//                    response.value.ok
//                }
//                is Resource.Failure -> throw exceptionErrorMapper.mapData(response)
//            }
//        }
//
//    override suspend fun deleteAccount(): Boolean =
//        safeApiCall {
//            userService.deleteAccount()
//        }.let { response ->
//            when (response) {
//                is Resource.Success -> {
//                    accountStorage.logOut()
//                    response.value.ok
//                }
//                is Resource.Failure -> throw exceptionErrorMapper.mapData(response)
//            }
//        }
//
//    override fun getUserEmail(): Pair<String, Boolean> =
//        accountStorage.getUserEmail()
//
//    override suspend fun contactSupport(message: String, email: String): Boolean =
//        safeApiCall {
//            userService.contactSupport(ContactSupportRequest(message, email))
//        }.let { response ->
//            when (response) {
//                is Resource.Success -> response.value.ok
//                is Resource.Failure -> throw exceptionErrorMapper.mapData(response)
//            }
//        }
//
//    override suspend fun checkPassword(password: String): Boolean =
//        safeApiCall {
//            userService.checkPassword(CheckPasswordRequest(password))
//        }.let { response ->
//            when (response) {
//                is Resource.Success -> response.value.isMatch
//                is Resource.Failure -> throw exceptionErrorMapper.mapData(response)
//            }
//        }
//
//
//    override suspend fun changePassword(currentPassword: String, newPassword: String): Boolean =
//        safeApiCall {
//            userService.changePassword(ChangePasswordRequest(currentPassword, newPassword))
//        }.let { response ->
//            when (response) {
//                is Resource.Success -> response.value.ok
//                is Resource.Failure -> throw exceptionErrorMapper.mapData(response)
//            }
//        }
//
//    override suspend fun changeEmail(currentEmail: String, newEmail: String): Boolean =
//        safeApiCall {
//            userService.changeEmail(ChangeEmailRequest(currentEmail, newEmail))
//        }.let { response ->
//            when (response) {
//                is Resource.Success -> response.value.ok
//                is Resource.Failure -> throw exceptionErrorMapper.mapData(response)
//            }
//        }
//
//    override suspend fun setFcmToken(): Boolean =
//        safeApiCall {
//            val token = accountStorage.fcmToken ?: ""
//            userService.setFcmToken(FCMTokenRequest(token))
//        }.let { response ->
//            when (response) {
//                is Resource.Success -> response.value.ok
//                is Resource.Failure -> throw exceptionErrorMapper.mapData(response)
//            }
//        }
}
