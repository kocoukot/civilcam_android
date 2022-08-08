package com.civilcam.domainLayer.repos

import android.net.Uri
import com.civilcam.domainLayer.model.UserBaseInfo
import com.civilcam.domainLayer.model.UserSetupModel

interface ProfileRepository {
	
	suspend fun getUserProfile(): UserBaseInfo
	
	suspend fun setUserProfile(userProfile: UserSetupModel): Boolean
	
	suspend fun updateUserProfile(userProfile: UserSetupModel): Boolean
	
	suspend fun editPhoneNumber(phone: String)
	
	suspend fun setUserAvatar(uri: Uri)
	
	suspend fun deleteUserAvatar()
}