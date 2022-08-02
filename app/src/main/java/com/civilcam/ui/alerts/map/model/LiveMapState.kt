package com.civilcam.ui.alerts.map.model

import com.civilcam.common.ext.compose.ComposeFragmentState
import com.civilcam.domain.model.alerts.UserAlertInformation
import com.civilcam.ui.emergency.model.EmergencyScreen

data class LiveMapState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val emergencyScreen: EmergencyScreen = EmergencyScreen.COUPLED,
    val isResolveAlertVisible: Boolean = false,
    val isResolved: Boolean = false,
    val userInformation: UserAlertInformation? = null
) : ComposeFragmentState