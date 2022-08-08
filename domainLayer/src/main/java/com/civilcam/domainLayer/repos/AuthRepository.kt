package com.civilcam.domainLayer.repos

import com.civilcam.domainLayer.model.CurrentUser


interface AuthRepository {

    var sessionToken: String

//    fun setFcmToken(token: String)

//    fun getFcmToken(): String?

//    suspend fun checkEmail(email: String): Boolean

    suspend fun signUp(email: String, password: String): CurrentUser

//    suspend fun signIn(email: String, password: String): CurrentUser
//
//    suspend fun resetPassword(email: String): Long
//
//    suspend fun recoverPassword(recoveryToken: String, newPassword: String): Boolean
//
//    suspend fun googleSignIn(authToken: String): CurrentUser
//
//    suspend fun facebookSignIn(accessToken: String): CurrentUser
//
////	fun sendPasswordOtpCode(email: String): Single<Long>
//
//    suspend fun verifyResetPasswordOtpCode(email: String, code: String): String
}