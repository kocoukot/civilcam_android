package com.civilcam.domainLayer.repos

import com.civilcam.domainLayer.model.CurrentUser
import io.reactivex.Maybe

interface AccountRepository {
	
	var sessionToken: String
	
	var isUserLoggedIn: Boolean
	
	fun loginUser(sessionToken: String, user: CurrentUser)
	
	fun getUser(): Maybe<CurrentUser>
}