package com.civilcam.data.local

import android.accounts.Account
import android.accounts.AccountManager
import com.civilcam.domainLayer.model.CurrentUser
import com.google.gson.Gson
import io.reactivex.Maybe
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
	
	fun logOut() {
		accountManager.apply {
			clearProfile()
			isUserLoggedIn = false
			sessionToken = null
		}
	}
	
	private fun clearProfile() = accountManager.setUserData(getOrCreateAccount(), USER, null)
	
	fun loginUser(sessionToken: String?, user: CurrentUser) {
		getOrCreateAccount()
			.let {
				accountManager.setAuthToken(it, SESSION_TOKEN, sessionToken)
				accountManager.setUserData(
					it,
					IS_USER_LOGGED_IN,
					(!user.sessionUser.isUserProfileSetupRequired).toString()
				)
				updateUser(it, user)
			}
	}
	
	private fun updateUser(currentAccount: Account, user: CurrentUser) =
		accountManager.setUserData(currentAccount, USER, gson.toJson(user))
	
	
	private fun getOrCreateAccount(): Account {
		return getAccount()
			?: run {
				Account(ACCOUNT_NAME, ACCOUNT_TYPE)
					.also { accountManager.addAccountExplicitly(it, ACCOUNT_PASSWORD, null) }
			}
	}
	
	private fun getAccount(): Account? =
		accountManager.getAccountsByType(ACCOUNT_TYPE).singleOrNull()
	
	
	fun getUser(): Maybe<CurrentUser> = Maybe.fromCallable {
		getAccount()
			?.let { account ->
				accountManager.getUserData(account, USER)
					?.takeIf { it.isNotEmpty() }
					?.let { gson.fromJson(it, CurrentUser::class.java) }
			}
	}
	
	companion object {
		private const val ACCOUNT_NAME = "CivilCam"
		private const val ACCOUNT_TYPE = "com.civilcam.civilcam"
		private const val ACCOUNT_PASSWORD = "Password"
		private const val USER = "$ACCOUNT_TYPE.user"
		
		private const val DEVICE_ID_TOKEN = "$ACCOUNT_TYPE.deviceid.token"
		private const val SESSION_TOKEN = "$ACCOUNT_TYPE.session.token"
		private const val IS_USER_LOGGED_IN = "$ACCOUNT_TYPE.user.isLoggedIn"
		
	}
}