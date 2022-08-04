package com.civilcam.data.repository

import com.civilcam.domain.model.CurrentUser

interface AccountRepository {

    var sessionToken: String

    var isUserLoggedIn: Boolean

    fun loginUser(sessionToken: String, user: CurrentUser)


}