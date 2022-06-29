package com.civilcam.ui.auth.password.create.model

import com.civilcam.common.ext.compose.ComposeFragmentRoute

sealed class CreatePasswordRoute : ComposeFragmentRoute {
	object GoBack : CreatePasswordRoute()
	object GoSave : CreatePasswordRoute()
}
