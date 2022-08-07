package com.civilcam.data.network.service

import com.civilcam.data.network.Endpoint
import com.civilcam.data.network.model.request.profile.ChangePhoneNumberRequest
import com.civilcam.data.network.model.request.profile.UserProfileRequest
import com.civilcam.data.network.model.response.profile.UserProfileResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface ProfileService {
	
	@GET(Endpoint.Profile.GET_USER_PROFILE)
	suspend fun getUserProfile(): UserProfileResponse
	
	@POST(Endpoint.Profile.SET_USER_PROFILE)
	suspend fun setUserProfile(@Body request: UserProfileRequest)
	
	@POST(Endpoint.Profile.UPDATE_USER_PROFILE)
	suspend fun updateProfile(@Body request: UserProfileRequest)
	
	@POST(Endpoint.Profile.CHANGE_USER_PHONE)
	suspend fun changeUserPhone(@Body request: ChangePhoneNumberRequest)
	
	@Multipart
	@POST(Endpoint.Profile.SET_USER_AVATAR)
	suspend fun setUserAvatar(@Part avatar: MultipartBody.Part): UserProfileResponse
	
	@DELETE(Endpoint.Profile.DELETE_USER_AVATAR)
	suspend fun deleteUserAvatar()
}