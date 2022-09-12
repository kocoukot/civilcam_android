package com.civilcam.ui.auth.create.model

import com.civilcam.domainLayer.model.user.CurrentUser
import com.civilcam.ext_features.compose.ComposeFragmentRoute

sealed class CreateAccountRoute : ComposeFragmentRoute {
	object GoBack : CreateAccountRoute()
	object GoLogin : CreateAccountRoute()
	data class GoContinue(val email: String) : CreateAccountRoute()

	data class GoSocialsLogin(val user: CurrentUser) : CreateAccountRoute()
	object OnGoogleSignIn : CreateAccountRoute()
	object OnFacebookSignIn : CreateAccountRoute()
}