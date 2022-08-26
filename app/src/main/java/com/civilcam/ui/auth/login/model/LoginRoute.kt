package com.civilcam.ui.auth.login.model

import com.civilcam.common.ext.compose.ComposeFragmentRoute
import com.civilcam.domainLayer.model.user.CurrentUser

sealed class LoginRoute : ComposeFragmentRoute {
    data class GoLogin(val user: CurrentUser) : LoginRoute()
    object GoReset : LoginRoute()
    object GoRegister : LoginRoute()
    object GoBack : LoginRoute()
    object OnGoogleSignIn : LoginRoute()
    object OnFacebookSignIn : LoginRoute()
}
