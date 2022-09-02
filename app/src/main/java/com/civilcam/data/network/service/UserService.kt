package com.civilcam.data.network.service

import com.civilcam.data.network.Endpoint
import com.civilcam.data.network.model.request.user.*
import com.civilcam.data.network.model.response.SuccessResponse
import com.civilcam.data.network.model.response.auth.UserResponse
import com.civilcam.data.network.model.response.user.CheckPasswordResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {
	
	@GET(Endpoint.User.CURRENT_USER)
	suspend fun currentUser(): UserResponse
	
	@POST(Endpoint.User.ACCEPT_TERMS_POLICY)
	suspend fun acceptTermsPolicy(@Body request: AcceptTermsRequest)
	
	@POST(Endpoint.User.SET_FCM_TOKEN)
	suspend fun setFcmToken(@Body request: FCMTokenRequest): SuccessResponse
	
	@POST(Endpoint.User.CHECK_PASSWORD)
	suspend fun checkPassword(@Body request: CheckPasswordRequest): CheckPasswordResponse
	
	@POST(Endpoint.User.CHANGE_PASSWORD)
	suspend fun changePassword(@Body request: ChangePasswordRequest): SuccessResponse
	
	@POST(Endpoint.User.CHANGE_EMAIL)
	suspend fun changeEmail(@Body request: ChangeEmailRequest): SuccessResponse
	
	@POST(Endpoint.User.SET_USER_LANGUAGE)
	suspend fun setUserLanguage(@Body request: SetUserLanguageRequest): UserResponse
	
	@POST(Endpoint.User.LOGOUT)
	suspend fun logout(): SuccessResponse
	
	@POST(Endpoint.User.CONTACT_SUPPORT)
	suspend fun contactSupport(@Body request: ContactSupportRequest): SuccessResponse
	
	@POST(Endpoint.User.TOGGLE_SETTINGS)
	suspend fun toggleSettings(@Body request: ToggleSettingsRequest): UserResponse
	
	@DELETE(Endpoint.User.DELETE_ACCOUNT)
	suspend fun deleteAccount(): SuccessResponse
	
	@POST(Endpoint.User.CHECK_PIN)
	suspend fun checkPin(@Body request: CheckPinRequest): CheckPasswordResponse
	
	@POST(Endpoint.User.SET_PIN)
	suspend fun setPin(@Body request: SetPinRequest): UserResponse
}