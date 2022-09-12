package com.civilcam.ui.auth.password.create.model

import com.civilcam.ext_features.compose.ComposeFragmentRoute

sealed class CreatePasswordRoute : ComposeFragmentRoute {
	object GoBack : CreatePasswordRoute()
	object GoSave : CreatePasswordRoute()
}