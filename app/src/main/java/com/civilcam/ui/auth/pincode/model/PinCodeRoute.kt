package com.civilcam.ui.auth.pincode.model

import com.civilcam.ext_features.compose.ComposeFragmentRoute

sealed class PinCodeRoute : ComposeFragmentRoute {
	object GoBack : PinCodeRoute()
	object GoGuardians : PinCodeRoute()
	object GoUserProfile : PinCodeRoute()
	object GoEmergency : PinCodeRoute()
}