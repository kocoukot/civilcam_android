package com.civilcam.data.repository

import com.civilcam.data.local.AccountStorage
import com.civilcam.data.local.SharedPreferencesStorage
import com.civilcam.data.mapper.auth.UserMapper
import com.civilcam.data.network.model.request.user.*
import com.civilcam.data.network.service.UserService
import com.civilcam.data.network.support.BaseRepository
import com.civilcam.data.network.support.Resource
import com.civilcam.domainLayer.model.user.CurrentUser
import com.civilcam.domainLayer.model.user.LanguageType
import com.civilcam.domainLayer.repos.UserRepository

class UserRepositoryImpl(
	private val sharedPreferencesStorage: SharedPreferencesStorage,
	private val userService: UserService,
	private val accountStorage: AccountStorage
) : UserRepository, BaseRepository() {
	
	private val userMapper = UserMapper()
	
	override suspend fun acceptTerms() =
		safeApiCall {
			userService.acceptTermsPolicy(AcceptTermsRequest(true))
		}.let { response ->
			when (response) {
				is Resource.Success -> response.value
				is Resource.Failure -> {
					response.checkIfLogOut { accountStorage.logOut() }
					throw response.serviceException
				}
			}
		}
	
	override suspend fun getCurrentUser(): CurrentUser =
		safeApiCall {
			userService.currentUser()
		}.let { response ->
			when (response) {
				is Resource.Success -> userMapper.mapData(response.value)
				is Resource.Failure -> {
                    response.checkIfLogOut { accountStorage.logOut() }
					throw response.serviceException
				}
			}
		}
	
	override suspend fun logout(): Boolean =
		safeApiCall {
			userService.logout()
		}.let { response ->
			accountStorage.logOut()
			when (response) {
				is Resource.Success -> response.value.ok
				is Resource.Failure -> throw response.serviceException
			}
			
		}
	
	override suspend fun deleteAccount(): Boolean =
		safeApiCall {
			userService.deleteAccount()
		}.let { response ->
			accountStorage.logOut()
			when (response) {
				is Resource.Success -> response.value.ok
				is Resource.Failure -> throw response.serviceException
			}
		}
	
	override suspend fun contactSupport(issue: String, text: String, email: String): Boolean =
		safeApiCall {
			userService.contactSupport(
				ContactSupportRequest(
					issueName = issue,
					message = text,
					replyTo = email
				)
			)
		}.let { response ->
			when (response) {
				is Resource.Success -> response.value.ok
				is Resource.Failure -> throw response.serviceException
			}
		}
	
	override suspend fun toggleSettings(type: String, isOn: Boolean): CurrentUser =
		safeApiCall {
			userService.toggleSettings(ToggleSettingsRequest(type, isOn))
		}.let { response ->
			when (response) {
				is Resource.Success -> userMapper.mapData(response.value)
					.also { accountStorage.updateUser(it) }
				is Resource.Failure -> throw response.serviceException
			}
		}
	
	override suspend fun setFcmToken(): Boolean =
		safeApiCall {
			accountStorage.fcmToken?.let {
				userService.setFcmToken(FCMTokenRequest(it))
			}
		}.let { response ->
			when (response) {
				is Resource.Success -> response.value?.ok ?: false
				is Resource.Failure -> throw response.serviceException
			}
		}
	
	override suspend fun checkPin(pinCode: String): Boolean =
		safeApiCall {
			userService.checkPin(CheckPinRequest(pinCode))
		}.let { response ->
			when (response) {
				is Resource.Success -> response.value.isMatch
				is Resource.Failure -> throw response.serviceException
			}
		}
	
	override suspend fun setPin(currentPinCode: String?, newPinCode: String): CurrentUser =
		safeApiCall {
			userService.setPin(SetPinRequest(currentPinCode, newPinCode))
		}.let { response ->
			when (response) {
				is Resource.Success -> userMapper.mapData(response.value)
					.also { accountStorage.updateUser(it) }
				is Resource.Failure -> throw response.serviceException
			}
		}
	
	
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
	override suspend fun checkPassword(password: String): Boolean =
		safeApiCall {
			userService.checkPassword(CheckPasswordRequest(password))
		}.let { response ->
			when (response) {
				is Resource.Success -> response.value.isMatch
				is Resource.Failure -> {
					if (response.serviceException.isForceLogout) accountStorage.logOut()
					throw response.serviceException
				}
			}
		}
	
	override suspend fun changePassword(currentPassword: String, newPassword: String): Boolean =
		safeApiCall {
			userService.changePassword(ChangePasswordRequest(currentPassword, newPassword))
		}.let { response ->
			when (response) {
				is Resource.Success -> response.value.ok
				is Resource.Failure -> {
					if (response.serviceException.isForceLogout) accountStorage.logOut()
					throw response.serviceException
				}
			}
		}
	
	override suspend fun setUserLanguage(languageType: LanguageType): CurrentUser =
		safeApiCall {
			userService.setUserLanguage(SetUserLanguageRequest(languageType))
		}.let { response ->
			when (response) {
				is Resource.Success -> userMapper.mapData(response.value)
				is Resource.Failure -> {
					if (response.serviceException.isForceLogout) accountStorage.logOut()
					throw response.serviceException
				}
			}
		}
	
	override suspend fun changeEmail(currentEmail: String, newEmail: String): Boolean =
		safeApiCall {
			userService.changeEmail(ChangeEmailRequest(currentEmail, newEmail))
		}.let { response ->
			when (response) {
				is Resource.Success -> response.value.ok
				is Resource.Failure -> {
					if (response.serviceException.isForceLogout) accountStorage.logOut()
					throw response.serviceException
				}
			}
		}

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
