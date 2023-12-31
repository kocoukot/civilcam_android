package com.civilcam.data.repository

import com.civilcam.data.local.AccountStorage
import com.civilcam.data.mapper.auth.UserMapper
import com.civilcam.data.network.model.request.auth.*
import com.civilcam.data.network.service.AuthService
import com.civilcam.data.network.service.GoogleOAuthService
import com.civilcam.data.network.support.BaseRepository
import com.civilcam.data.network.support.Resource
import com.civilcam.domainLayer.ServiceException
import com.civilcam.domainLayer.model.user.CurrentUser
import com.civilcam.domainLayer.repos.AuthRepository
import timber.log.Timber


class AuthRepositoryImpl(
    private val accountStorage: AccountStorage,
    private val authService: AuthService,
    private val googleOAuthService: GoogleOAuthService
) : AuthRepository, BaseRepository() {

	private val sessionUserMapper = UserMapper()

	override var sessionToken: String = ""
		get() = accountStorage.sessionToken.orEmpty()
		set(value) {
			field = value
			accountStorage.sessionToken = value
		}

	override fun setFcmToken(token: String) {
		accountStorage.fcmToken = token
	}

	override fun getFcmToken(): String? = accountStorage.fcmToken

	override suspend fun checkEmail(email: String): Boolean =
		safeApiCall {
			authService.checkUserExists(
				CheckEmailRequest(
					email = email
				)
			)
		}.let { response ->
			when (response) {
				is Resource.Success -> response.value.exists
                is Resource.Failure -> {
                    response.checkIfLogOut { accountStorage.logOut() }
                    throw response.serviceException
                }
			}
		}


	override suspend fun signUp(email: String, password: String): CurrentUser =
		safeApiCall {
			authService.signUp(
				SignUpRequest(email = email, password = password)
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


	override suspend fun signIn(email: String, password: String): CurrentUser =
		safeApiCall {
			authService.signIn(
				SignInRequest(
					email = email,
					password = password,
					accountStorage.fcmToken
				)
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

	override suspend fun resetPassword(email: String): Long =
		safeApiCall {
			authService.resetPassword(
				ResetPasswordRequest(
					email = email,
				)
			)
		}.let { response ->
			when (response) {
                is Resource.Success -> response.value.timeout.toLong()
                is Resource.Failure -> {
                    throw response.serviceException
                }
			}
		}

	override suspend fun recoverPassword(recoveryToken: String, newPassword: String): Boolean =
		safeApiCall {
			authService.recoverPassword(
				RecoverPasswordRequest(
					recoveryToken = recoveryToken,
					newPassword = newPassword,
				)
			)
		}.let { response ->
			when (response) {
                is Resource.Success -> response.value.ok
                is Resource.Failure -> {
                    throw response.serviceException
                }
			}
		}

	override suspend fun googleSignIn(authToken: String): CurrentUser =
		safeApiCall {
			val googleAuthResponse =
				googleOAuthService.signOAuth(GoogleOAuthRequest(code = authToken))
			val gAccessToken = googleAuthResponse.map { it.accessToken }
				.doOnError {
					throw ServiceException(
						errorMessage = it.localizedMessage?.toString()
							?: "Some error"
					)
				}
				.blockingGet()

            Timber.i("gAccessToken gAccessToken $gAccessToken \n authToken $authToken")

            authService.googleSignIn(SocialLoginRequest(gAccessToken))
        }.let { response ->
            when (response) {
                is Resource.Success -> sessionUserMapper.mapData(response.value)
                is Resource.Failure -> throw response.serviceException
            }
        }


    override suspend fun facebookSignIn(accessToken: String): CurrentUser =
        safeApiCall {
            authService.facebookSignIn(SocialLoginRequest(accessToken))
        }.let { response ->
            when (response) {
                is Resource.Success -> sessionUserMapper.mapData(response.value)
                is Resource.Failure -> throw response.serviceException
            }
        }

    override suspend fun verifyResetPasswordOtpCode(email: String, code: String): String =
        safeApiCall {
            authService.verifyResetPasswordOtp(
                VerifyResetPasswordRequest(email, code)
            )
        }.let { response ->
            when (response) {
                is Resource.Success -> response.value.recoveryToken
                is Resource.Failure -> throw response.serviceException

            }
		}


//	override fun sendPasswordOtpCode(email: String): Single<Long> =
//		authService.sendPasswordOtpCode(SendPasswordOtpCodeRequest(email))
//			.onErrorResumeNext(errorHandler::onSingleError)
//			.map { it.timeout }
//
//	override fun verifyResetPasswordOtpCode(email: String, code: String): Single<String> =
//		authService.verifyResetPasswordOtpCode(VerifyResetPasswordRequest(email, code))
//			.onErrorResumeNext(errorHandler::onSingleError)
//			.map { it.recoveryToken }
//
//	override fun recoverPassword(recoveryToken: String, newPassword: String): Single<Boolean> =
//		authService.recoverPassword(RecoverPasswordRequest(recoveryToken, newPassword))
//			.onErrorResumeNext(errorHandler::onSingleError)
//			.map { it.ok }
}