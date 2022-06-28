package com.civilcam.ui.auth.password.create.model

import com.civilcam.common.ext.compose.ComposeFragmentRoute

sealed class CreatePasswordRoute : ComposeFragmentRoute {
	object GoLogin : CreatePasswordRoute()
	object GoReset : CreatePasswordRoute()
	object GoRegister : CreatePasswordRoute()
}
