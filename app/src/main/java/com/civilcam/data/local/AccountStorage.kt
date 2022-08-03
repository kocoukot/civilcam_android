package com.civilcam.data.local

import android.accounts.Account
import android.accounts.AccountManager
import com.google.gson.Gson
import java.util.*


class AccountStorage(
    private val accountManager: AccountManager,
    private val gson: Gson,
    private val sharedPreferencesStorage: SharedPreferencesStorage,
) {
    var sessionToken: String?
        get() = accountManager.peekAuthToken(getOrCreateAccount(), SESSION_TOKEN)
        set(value) {
            accountManager.setAuthToken(getOrCreateAccount(), SESSION_TOKEN, value)
        }

    val deviceIdToken: String
        get() = accountManager.peekAuthToken(getOrCreateAccount(), DEVICE_ID_TOKEN) ?: run {
            val deviceId = UUID.randomUUID().toString()
            accountManager.setAuthToken(getOrCreateAccount(), DEVICE_ID_TOKEN, deviceId)
            deviceId
        }

    var isUserLoggedIn: Boolean
        get() {
            return getAccount()
                ?.let { account ->
                    accountManager.getUserData(account, IS_USER_LOGGED_IN)
                        ?.toBoolean()
                        ?: run {
                            val isLoggedIn = !sessionToken.isNullOrEmpty()
                            accountManager.setUserData(
                                account,
                                IS_USER_LOGGED_IN,
                                isLoggedIn.toString()
                            )
                            isLoggedIn
                        }
                }
                ?: false
        }
        set(value) {
            accountManager.setUserData(getOrCreateAccount(), IS_USER_LOGGED_IN, value.toString())
        }


    private fun getOrCreateAccount(): Account {
        return getAccount()
            ?: run {
                Account(ACCOUNT_NAME, ACCOUNT_TYPE)
                    .also { accountManager.addAccountExplicitly(it, ACCOUNT_PASSWORD, null) }
            }
    }

    private fun getAccount(): Account? =
        accountManager.getAccountsByType(ACCOUNT_TYPE).singleOrNull()


    companion object {
        private const val ACCOUNT_NAME = "CivilCam"
        private const val ACCOUNT_TYPE = "com.civilcam.civilcam"
        private const val ACCOUNT_PASSWORD = "Password"
        private const val DEVICE_ID_TOKEN = "$ACCOUNT_TYPE.deviceid.token"
        private const val SESSION_TOKEN = "$ACCOUNT_TYPE.session.token"
        private const val IS_USER_LOGGED_IN = "$ACCOUNT_TYPE.user.isLoggedIn"

    }
}