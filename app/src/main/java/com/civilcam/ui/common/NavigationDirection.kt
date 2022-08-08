package com.civilcam.ui.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class NavigationDirection : Parcelable {
	@Parcelize
	object SignInSuccess : NavigationDirection()
	
	@Parcelize
	object ProfileSetup : NavigationDirection()
	
	@Parcelize
	class EmailVerification(val email: String) : NavigationDirection()
	
	companion object {
		fun resolveDirectionFor(user: com.civilcam.domainLayer.model.CurrentUser?) = user.let {
			when {
				else -> SignInSuccess
			}
		}
	}
}