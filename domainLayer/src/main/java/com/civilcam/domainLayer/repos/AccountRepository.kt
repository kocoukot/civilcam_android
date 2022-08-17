package com.civilcam.domainLayer.repos

import com.civilcam.domainLayer.model.CurrentUser

interface AccountRepository {
	
	var sessionToken: String
	
	var isUserLoggedIn: Boolean
	
	fun loginUser(sessionToken: String, user: CurrentUser)
	
	fun getUser(): CurrentUser
}