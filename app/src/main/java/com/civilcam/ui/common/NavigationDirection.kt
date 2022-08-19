package com.civilcam.ui.common

import android.os.Parcelable
import com.civilcam.domainLayer.model.CurrentUser
import com.civilcam.domainLayer.model.AuthType
import com.civilcam.domainLayer.model.user.CurrentUser
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
	
	@Parcelize
	class PhoneVerification(val phone: String) : NavigationDirection()
	
	companion object {
		fun resolveDirectionFor(user: CurrentUser) = user.let {
			when {
				!user.sessionUser.isEmailVerified -> EmailVerification(user.sessionUser.email)
				!user.sessionUser.isTermsAndPolicyAccepted -> TermsAndPolicyAccept //&& it.sessionUser.authType == AuthType.email
				user.sessionUser.isUserProfileSetupRequired -> ProfileSetup
				//				!user.userBaseInfo.isPhoneVerified -> PhoneVerification(user.userBaseInfo.phone)

				else -> SignInSuccess
			}
		}
	}
}