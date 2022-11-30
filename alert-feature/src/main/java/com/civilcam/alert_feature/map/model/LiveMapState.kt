package com.civilcam.alert_feature.map.model

import com.civilcam.domainLayer.EmergencyScreen
import com.civilcam.domainLayer.model.OnGuardUserData
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
) : ComposeFragmentState