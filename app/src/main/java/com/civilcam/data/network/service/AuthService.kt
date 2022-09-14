package com.civilcam.data.network.service

import com.civilcam.data.network.Endpoint
import com.civilcam.data.network.model.request.auth.*
import com.civilcam.data.network.model.response.IsSentResponse
import com.civilcam.data.network.model.response.SuccessResponse
import com.civilcam.data.network.model.response.auth.CheckEmailResponse
import com.civilcam.data.network.model.response.auth.RecoveryTokenResponse
import com.civilcam.data.network.model.response.auth.UserResponse
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthService {

	@POST(Endpoint.Auth.CHECK_USER)
	suspend fun checkUserExists(@Body request: CheckEmailRequest): CheckEmailResponse

	@POST(Endpoint.Auth.SIGN_UP)
	suspend fun signUp(@Body request: SignUpRequest): UserResponse

	@POST(Endpoint.Auth.SIGN_IN)
	suspend fun signIn(@Body request: SignInRequest): UserResponse

	@POST(Endpoint.Auth.GOOGLE_SIGN_IN)
	suspend fun googleSignIn(@Body request: SocialLoginRequest): UserResponse

	@POST(Endpoint.Auth.FACEBOOK_SIGN_IN)
	suspend fun facebookSignIn(@Body request: SocialLoginRequest): UserResponse

	@POST(Endpoint.Auth.RESET_PASSWORD)
	suspend fun resetPassword(@Body request: ResetPasswordRequest): IsSentResponse

	@POST(Endpoint.Auth.VERIFY_RESET_PASSWORD_OTP)
	suspend fun verifyResetPasswordOtp(@Body request: VerifyResetPasswordRequest): RecoveryTokenResponse

	@POST(Endpoint.Auth.RECOVER_PASSWORD)
	suspend fun recoverPassword(@Body request: RecoverPasswordRequest): SuccessResponse
}