package com.civilcam.data.local

import android.accounts.Account
import android.accounts.AccountManager
import com.civilcam.domainLayer.model.user.CurrentUser
import com.civilcam.domainLayer.model.user.UserBaseInfo
import com.civilcam.domainLayer.model.user.UserState
import com.google.gson.Gson
import java.util.*


class AccountStorage(
	private val accountManager: AccountManager,
	private val gson: Gson,
	private val sharedPreferencesStorage: SharedPreferencesStorage,
) {
	var streamKey: String
		get() = accountManager.peekAuthToken(getOrCreateAccount(), STREAM_KEY).orEmpty()
		set(value) {
			accountManager.setAuthToken(getOrCreateAccount(), STREAM_KEY, value)
		}

	var sessionToken: String?
		get() = accountManager.peekAuthToken(getOrCreateAccount(), SESSION_TOKEN)
		set(value) {
			accountManager.setAuthToken(getOrCreateAccount(), SESSION_TOKEN, value)
		}

	var fcmToken: String?
		get() = sharedPreferencesStorage.get<String>(SharedPreferencesStorage.fcmTokenKey)
		set(value) {
			sharedPreferencesStorage[SharedPreferencesStorage.fcmTokenKey] = value
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

	fun updateUser(user: CurrentUser) =
		accountManager.setUserData(getOrCreateAccount(), USER, gson.toJson(user))

	fun updateUserBaseInfo(userBaseInfo: UserBaseInfo) =
		getUser()?.copy(userBaseInfo = userBaseInfo)?.let { updateUser(it) }


	private fun updateUser(currentAccount: Account, user: CurrentUser) =
		accountManager.setUserData(currentAccount, USER, gson.toJson(user))

	fun setSosState(isSos: Boolean) = setSosState(getOrCreateAccount(), isSos)

	private fun setSosState(
		currentAccount: Account,
		isSos: Boolean,
	) {
		var user = getUser()
		user = user?.copy(
			sessionUser = user.sessionUser.copy(
				userState = UserState.resolveState(isSos)
			)
		)
		accountManager.setUserData(currentAccount, USER, gson.toJson(user))
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

	fun getUser(): CurrentUser? =
		getAccount().let { account ->
			accountManager.getUserData(account, USER)
				.takeIf { it.isNotEmpty() }
				.let { gson.fromJson(it, CurrentUser::class.java) }
		}


	fun setExpiredSubState() {
		getUser()?.let { user ->
			val newUser =
				user.copy(sessionUser = user.sessionUser.copy(isSubscriptionActive = false))
			updateUser(newUser)
		}
	}

	companion object {
		private const val ACCOUNT_NAME = "CivilCam"
		private const val ACCOUNT_TYPE = "com.civilcam.civilcam"
		private const val ACCOUNT_PASSWORD = "Password"
		private const val USER = "$ACCOUNT_TYPE.user"

		private const val DEVICE_ID_TOKEN = "$ACCOUNT_TYPE.deviceid.token"
		private const val SESSION_TOKEN = "$ACCOUNT_TYPE.session.token"
		private const val STREAM_KEY = "$ACCOUNT_TYPE.stream.key"

		private const val IS_USER_LOGGED_IN = "$ACCOUNT_TYPE.user.isLoggedIn"
		private const val IS_SOS_STATE = "$ACCOUNT_TYPE.sos.state"

	}
}