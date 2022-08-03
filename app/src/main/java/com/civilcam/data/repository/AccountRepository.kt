package com.civilcam.data.repository

interface AccountRepository {

    var sessionToken: String

    var isUserLoggedIn: Boolean

}