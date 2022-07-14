package com.civilcam.ui.emergency.model

import com.civilcam.common.ext.compose.ComposeFragmentRoute

sealed class EmergencyRoute : ComposeFragmentRoute {
	object GoUserProfile : EmergencyRoute()
	object GoSettings : EmergencyRoute()
	object GoPinCode : EmergencyRoute()
	object CheckPermission : EmergencyRoute()
	data class ControlFlash(val enabled: Boolean) : EmergencyRoute()
}