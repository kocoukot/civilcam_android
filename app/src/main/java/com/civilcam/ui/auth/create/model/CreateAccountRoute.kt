package com.civilcam.ui.auth.create.model

import com.civilcam.common.ext.compose.ComposeFragmentRoute

sealed class CreateAccountRoute : ComposeFragmentRoute {
	object GoBack : CreateAccountRoute()
	object GoLogin : CreateAccountRoute()
	object GoContinue : CreateAccountRoute()
}