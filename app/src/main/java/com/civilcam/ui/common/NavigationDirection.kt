package com.civilcam.ui.common

import android.os.Parcelable
import com.civilcam.domainLayer.model.AuthType
import com.civilcam.domainLayer.model.CurrentUser
import kotlinx.parcelize.Parcelize

sealed class NavigationDirection : Parcelable {
	@Parcelize
	object SignInSuccess : NavigationDirection()
	
	@Parcelize
	object ProfileSetup : NavigationDirection()
	
	@Parcelize
	object TermsAndPolicyAccept : NavigationDirection()
	
	@Parcelize
	class EmailVerification(val email: String) : NavigationDirection()
	
	companion object {
		fun resolveDirectionFor(user: CurrentUser) = user.let {
			when {
				!user.sessionUser.isEmailVerified -> EmailVerification(user.sessionUser.email)
				!user.sessionUser.isTermsAndPolicyAccepted && it.sessionUser.authType == AuthType.EMAIL -> TermsAndPolicyAccept
				user.sessionUser.isUserProfileSetupRequired -> ProfileSetup
				else -> SignInSuccess
			}
		}
	}
}