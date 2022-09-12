package com.civilcam.ui.auth.login.model

import com.civilcam.domainLayer.model.user.CurrentUser
import com.civilcam.ext_features.compose.ComposeFragmentRoute

sealed class LoginRoute : ComposeFragmentRoute {
    data class GoLogin(val user: CurrentUser) : LoginRoute()
    object GoReset : LoginRoute()
    object GoRegister : LoginRoute()
    object GoBack : LoginRoute()
    object OnGoogleSignIn : LoginRoute()
    object OnFacebookSignIn : LoginRoute()
}
