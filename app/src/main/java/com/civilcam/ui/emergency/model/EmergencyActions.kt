package com.civilcam.ui.emergency.model

import com.civilcam.domainLayer.EmergencyScreen
import com.civilcam.ext_features.compose.ComposeFragmentActions

sealed class EmergencyActions : ComposeFragmentActions {
	object DoubleClickSos : EmergencyActions()
	object DisableSos : EmergencyActions()
	object OneClickSafe : EmergencyActions()
	object GoUserProfile : EmergencyActions()
	object GoSettings : EmergencyActions()
	object GoBack : EmergencyActions()
	object DetectLocation : EmergencyActions()
	object ClickCloseAlert : EmergencyActions()
	object ChangeLiveScreen : EmergencyActions()
	object ControlTorch : EmergencyActions()

	data class ChangeCurrentTime(val time: Long) : EmergencyActions()
	data class ClickChangeScreen(val screenState: EmergencyScreen) : EmergencyActions()
}