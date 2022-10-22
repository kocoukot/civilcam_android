package com.civilcam.common.ext

import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.civilcam.R
import com.civilcam.domainLayer.model.VerificationFlow
import com.civilcam.ui.auth.pincode.PinCodeFragment
import com.civilcam.ui.auth.pincode.model.PinCodeFlow
import com.civilcam.ui.common.NavigationDirection
import com.civilcam.ui.terms.TermsFragment
import com.civilcam.ui.verification.VerificationFragment
import com.google.android.gms.tasks.Task
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun NavController.navigateToRoot(
	@IdRes rootScreen: Int,
	@IdRes vararg backStack: Int = intArrayOf()
) {
	backStack.forEachIndexed { index, screen ->
		if (index == 0) {
			navigate(
				screen, null,
				NavOptions
					.Builder()
					.setPopUpTo(R.id.nav_graph, false)
					.build()
			)
		} else {
			navigate(screen)
		}
	}
	navigate(
		rootScreen, null,
		NavOptions.Builder()
			.setPopUpTo(backStack.lastOrNull() ?: R.id.nav_graph, false)
			.build()
	)
}

fun NavController.navigateByDirection(
	direction: NavigationDirection
) {
	when (direction) {
		is NavigationDirection.SignInSuccess -> {
			navigateToRoot(R.id.emergency_root)
		}
		is NavigationDirection.EmailVerification -> {
			navigate(
				R.id.verificationFragment,
				VerificationFragment.createArgs(
                    VerificationFlow.CURRENT_EMAIL,
                    direction.email,
                    ""
                )
			)
		}
		is NavigationDirection.ProfileSetup -> {
			navigate(
				R.id.profileSetupFragment,
			)
		}
		is NavigationDirection.PinCodeSetup -> {
			navigate(
				R.id.pinCodeFragment,
				PinCodeFragment.createArgs(PinCodeFlow.CREATE_PIN_CODE, true)
			)
		}
		is NavigationDirection.TermsAndPolicyAccept -> {
			navigate(
				R.id.termsFragment,
                TermsFragment.createArgs()
			)
		}
		is NavigationDirection.PhoneVerification -> {
//			navigate(
//				R.id.verificationFragment,
//				VerificationFragment.createArgs(
//					VerificationFlow.NEW_PHONE,
//					direction.phone
//				)
//			)
		}
	}
}

suspend fun <T> Task<T>.awaitResult() = suspendCoroutine<T?> { continuation ->
    if (isComplete) {
        if (isSuccessful) continuation.resume(this.result)
        else continuation.resume(null)
        return@suspendCoroutine
    }
    addOnSuccessListener { continuation.resume(this.result) }
    addOnFailureListener { continuation.resume(null) }
    addOnCanceledListener { continuation.resume(null) }
}