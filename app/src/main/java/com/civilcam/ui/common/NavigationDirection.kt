package com.civilcam.ui.common

import android.os.Parcelable
import com.civilcam.domainLayer.model.subscription.UserSubscriptionState
import com.civilcam.domainLayer.model.user.CurrentUser
import kotlinx.parcelize.Parcelize

sealed class NavigationDirection : Parcelable {
	@Parcelize
	object SignInSuccess : NavigationDirection()
	
	@Parcelize
	object ProfileSetup : NavigationDirection()
	
	@Parcelize
	object PinCodeSetup : NavigationDirection()

	@Parcelize
	object TermsAndPolicyAccept : NavigationDirection()

	@Parcelize
	class EmailVerification(val email: String) : NavigationDirection()

	@Parcelize
	class PhoneVerification(val phone: String) : NavigationDirection()

	@Parcelize
	class SubscriptionNotActive(val state: UserSubscriptionState) : NavigationDirection()

	companion object {
		fun resolveDirectionFor(user: CurrentUser) = user.let {
			when {
				!user.sessionUser.isEmailVerified -> EmailVerification(user.sessionUser.email)
				!user.sessionUser.isTermsAndPolicyAccepted -> TermsAndPolicyAccept //&& it.sessionUser.authType == AuthType.email
				user.sessionUser.isUserProfileSetupRequired -> ProfileSetup
//				user.subscription.status != SubscriptionStatus.active && !user.sessionUser.isUserProfileSetupRequired -> SubscriptionNotActive
				//todo fix may be latter
				!user.sessionUser.isSubscriptionActive -> SubscriptionNotActive(
					state = if (user.sessionUser.isUserProfileSetupRequired)
						UserSubscriptionState.FIRST_LAUNCH
					else
						UserSubscriptionState.SUB_EXPIRED
				)
				!user.sessionUser.isPinCodeSet -> PinCodeSetup
				//				!user.userBaseInfo.isPhoneVerified -> PhoneVerification(user.userBaseInfo.phone)
				else -> SignInSuccess
			}
		}
	}
}