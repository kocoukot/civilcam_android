package com.civilcam.domainLayer.repos

import com.civilcam.domainLayer.model.user.CurrentUser
import com.civilcam.domainLayer.model.user.LanguageType
import com.google.android.gms.maps.model.LatLng


interface UserRepository {
	
	suspend fun acceptTerms()
	
	suspend fun getCurrentUser(): CurrentUser
	
	suspend fun logout(): Boolean
	
	suspend fun changeEmail(currentEmail: String, newEmail: String): Boolean
	
	suspend fun checkPassword(password: String): Boolean
	
	suspend fun changePassword(currentPassword: String, newPassword: String): Boolean
	
	suspend fun setUserLanguage(languageType: LanguageType): CurrentUser
	
	suspend fun deleteAccount(): Boolean

    suspend fun contactSupport(issue: String, text: String, email: String): Boolean

    suspend fun toggleSettings(type: String, isOn: Boolean): CurrentUser

    suspend fun setFcmToken(): Boolean

    suspend fun checkPin(pinCode: String): Boolean

    suspend fun setPin(currentPinCode: String?, newPinCode: String): CurrentUser

    suspend fun setSafeState(pinCode: String, coords: LatLng): Boolean

	suspend fun setUserCoords(coords: LatLng): Boolean
}