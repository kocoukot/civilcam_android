package com.civilcam.ui.auth.pincode.model

import com.civilcam.common.ext.compose.ComposeFragmentRoute

sealed class PinCodeRoute : ComposeFragmentRoute {
	object GoBack : PinCodeRoute()
	object GoGuardians : PinCodeRoute()
}