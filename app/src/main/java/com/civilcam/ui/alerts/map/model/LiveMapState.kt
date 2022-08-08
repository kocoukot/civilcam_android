package com.civilcam.ui.alerts.map.model

import androidx.camera.core.CameraSelector
import com.civilcam.common.ext.compose.ComposeFragmentState
import com.civilcam.ui.emergency.model.EmergencyScreen

data class LiveMapState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val emergencyScreen: EmergencyScreen = EmergencyScreen.COUPLED,
    val isResolveAlertVisible: Boolean = false,
    val isResolved: Boolean = false,
    val userInformation: com.civilcam.domainLayer.model.alerts.UserAlertInformation? = null,
    val cameraState: Int = CameraSelector.LENS_FACING_BACK, // todo fix later
) : ComposeFragmentState