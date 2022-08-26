package com.civilcam.domainLayer.repos

import com.civilcam.domainLayer.model.user.CurrentUser
import com.civilcam.domainLayer.model.user.LanguageType


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
}