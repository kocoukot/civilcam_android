package com.civilcam.data.repository

import android.net.Uri
import com.civilcam.domain.model.UserBaseInfo
import com.civilcam.domain.model.UserSetupModel

interface ProfileRepository {
	
	suspend fun getUserProfile(): UserBaseInfo
	
	suspend fun setUserProfile(userProfile: UserSetupModel): Boolean
	
	suspend fun updateUserProfile(userProfile: UserSetupModel): Boolean
	
	suspend fun editPhoneNumber(phone: String)
	
	suspend fun setUserAvatar(uri: Uri)
	
	suspend fun deleteUserAvatar()
}