package com.civilcam.data.repository

import com.civilcam.data.local.AccountStorage
import com.civilcam.domainLayer.model.CurrentUser
import com.civilcam.domainLayer.repos.AccountRepository

class AccountRepositoryImpl(
    private val accountStorage: AccountStorage
) : AccountRepository {

    override var sessionToken: String = ""
        get() = accountStorage.sessionToken.orEmpty()
        set(value) {
            field = value
            accountStorage.sessionToken = value
        }

    override var isUserLoggedIn: Boolean = false
        get() = accountStorage.isUserLoggedIn
        set(value) {
            field = value
            accountStorage.isUserLoggedIn = value
        }

    override fun loginUser(sessionToken: String, user: CurrentUser) {
        accountStorage.loginUser(sessionToken, user)
    }
    
}
