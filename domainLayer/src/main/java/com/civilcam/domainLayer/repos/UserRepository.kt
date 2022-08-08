package com.civilcam.domainLayer.repos

import com.civilcam.domainLayer.model.CurrentUser


interface UserRepository {

	suspend fun acceptTerms()

	suspend fun getCurrentUser(): CurrentUser

	suspend fun signOut(): Boolean
//
//	suspend fun deleteAccount(): Boolean
//
//	fun getUserEmail(): Pair<String, Boolean>
//
//	suspend fun contactSupport(message: String, email: String): Boolean
//
//	suspend fun checkPassword(password: String): Boolean
//
//	suspend fun changePassword(currentPassword: String, newPassword: String): Boolean
//
//	suspend fun changeEmail(currentEmail: String, newEmail: String): Boolean
//
//	suspend fun setFcmToken(): Boolean
}