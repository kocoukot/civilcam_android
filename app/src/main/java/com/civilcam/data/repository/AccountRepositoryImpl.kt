package com.civilcam.data.repository

import com.civilcam.data.local.AccountStorage

class AccountRepositoryImpl(
    private val accountStorage: AccountStorage
) : com.civilcam.domainLayer.repos.AccountRepository {

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

    override fun loginUser(sessionToken: String, user: com.civilcam.domainLayer.model.CurrentUser) {
        accountStorage.loginUser(sessionToken, user)
    }

}
