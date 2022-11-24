package com.civilcam.alert_feature.map.model

import androidx.camera.core.CameraSelector
import com.civilcam.domainLayer.EmergencyScreen
import com.civilcam.domainLayer.model.alerts.AlertDetailModel
import com.civilcam.ext_features.compose.ComposeFragmentState

data class LiveMapState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val emergencyScreen: EmergencyScreen = EmergencyScreen.COUPLED,
    val isResolveAlertVisible: Boolean = false,
    val isResolved: Boolean = false,
    val isLocationAllowed: Boolean = false,
    val onGuardUserInformation: OnGuardUserData? = null,
    val currentUserLocationData: UserAlertLocationData? = null,
    val cameraState: Int = CameraSelector.LENS_FACING_BACK, // todo fix later
    val liveUrl: String? = null,
    val alertDetailModel: AlertDetailModel? = null
) : ComposeFragmentState