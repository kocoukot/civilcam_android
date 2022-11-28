package com.civilcam.ui.emergency.model

import com.civilcam.domainLayer.EmergencyScreen

sealed class EmergencyActions {
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
	object CloseToast : EmergencyActions()
	
	data class ClickChangeScreen(val screenState: EmergencyScreen) : EmergencyActions()
	data class LiveCurrentTime(val time: String) : EmergencyActions()
	
}