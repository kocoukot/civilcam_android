package com.civilcam.domainLayer.repos

import com.civilcam.domainLayer.model.user.CurrentUser

interface AccountRepository {

    var sessionToken: String

    var isUserLoggedIn: Boolean

    val deviceIdToken: String

    fun loginUser(sessionToken: String, user: CurrentUser)

    fun getUser(): CurrentUser
}