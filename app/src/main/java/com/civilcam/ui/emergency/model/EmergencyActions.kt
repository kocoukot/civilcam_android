package com.civilcam.ui.emergency.model

import androidx.camera.core.CameraInfo
import com.civilcam.common.ext.compose.ComposeFragmentActions

sealed class EmergencyActions : ComposeFragmentActions {
	object DoubleClickSos : EmergencyActions()
	object DisableSos : EmergencyActions()
	object OneClickSafe : EmergencyActions()
	object GoUserProfile : EmergencyActions()
	object GoSettings : EmergencyActions()
	object GoBack : EmergencyActions()
	object DetectLocation : EmergencyActions()
	object ControlFlash : EmergencyActions()
	object ChangeCamera : EmergencyActions()
	
	data class CameraInitialized(val cameraLensInfo: HashMap<Int, CameraInfo>) :
		EmergencyActions()
	
	data class ClickChangeScreen(val screenState: EmergencyScreen) : EmergencyActions()
	
	data class ChangeMode(val mode: EmergencyScreen) : EmergencyActions()
}
