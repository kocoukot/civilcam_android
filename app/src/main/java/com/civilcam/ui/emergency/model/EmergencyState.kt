package com.civilcam.ui.emergency.model

import androidx.camera.core.CameraSelector
import com.civilcam.common.ext.compose.ComposeFragmentState
import com.civilcam.domain.model.alerts.UserEmergencyState

data class EmergencyState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val location: String = "12564 Nox Street ...",
    val cameraFlash: Boolean = true,
    val isLocationAllowed: Boolean = false,
    val cameraState: Int = CameraSelector.LENS_FACING_BACK,
    val emergencyScreen: EmergencyScreen = EmergencyScreen.NORMAL,
    val emergencyState: UserEmergencyState = UserEmergencyState.IN_SAFE,
    val emergencyButton: EmergencyButton = EmergencyButton.InSafeButton
) : ComposeFragmentState