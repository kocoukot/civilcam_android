package com.civilcam.ui.profile.userProfile.model

import com.civilcam.R
import com.civilcam.ext_features.arch.ComposeRouteNavigation
import com.civilcam.ext_features.compose.ComposeFragmentRoute
import com.civilcam.ui.auth.pincode.PinCodeFragment
import com.civilcam.ui.auth.pincode.model.PinCodeFlow
import com.civilcam.ui.profile.credentials.ChangeCredentialsFragment

sealed class UserProfileRoute : ComposeFragmentRoute {
    object GoBack : UserProfileRoute() {
        override fun handleRoute(): ComposeRouteNavigation = ComposeRouteNavigation.PopNavigation
    }

    object GoPinCode : UserProfileRoute() {
        override fun handleRoute(): ComposeRouteNavigation = ComposeRouteNavigation.GraphNavigate(
            R.id.pinCodeFragment,
            PinCodeFragment.createArgs(PinCodeFlow.CURRENT_PIN_CODE, false)
        )
    }

    data class GoCredentials(
        private val userProfileType: UserProfileType,
        private val credential: String?
    ) :
        UserProfileRoute() {
        override fun handleRoute(): ComposeRouteNavigation =
            ComposeRouteNavigation.GraphNavigate(
                R.id.changeCredentialsFragment,
                ChangeCredentialsFragment.createArgs(
                    userProfileType,
                    credential.orEmpty()
                )
            )
    }
}
