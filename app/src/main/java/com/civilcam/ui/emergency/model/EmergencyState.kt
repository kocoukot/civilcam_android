package com.civilcam.ui.emergency.model

import androidx.camera.core.CameraInfo
import androidx.camera.core.CameraSelector
import androidx.camera.core.TorchState
import com.civilcam.common.ext.compose.ComposeFragmentState
import com.civilcam.domainLayer.model.alerts.UserEmergencyState

data class EmergencyState(
	val isLoading: Boolean = false,
	val errorText: String = "",
	val location: String = "12564 Nox Street ...",
	val cameraFlash: Boolean = false,
	val isLocationAllowed: Boolean = false,
	val cameraState: Int = CameraSelector.LENS_FACING_BACK,
	val emergencyScreen: EmergencyScreen = EmergencyScreen.NORMAL,
	val emergencyState: UserEmergencyState = UserEmergencyState.IN_SAFE,
	val emergencyButton: EmergencyButton = EmergencyButton.InSafeButton,
	val lens: Int? = CameraSelector.LENS_FACING_BACK,
	val lensInfo: MutableMap<Int, CameraInfo> = mutableMapOf(),
	@TorchState.State val torchState: Int = TorchState.OFF,
) : ComposeFragmentState