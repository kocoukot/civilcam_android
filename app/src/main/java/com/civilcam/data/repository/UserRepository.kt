package com.civilcam.data.repository

interface UserRepository {

    suspend fun acceptTerms()

//	suspend fun signOut(): Boolean
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