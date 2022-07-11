package com.civilcam.ui.emergency.model

import com.civilcam.common.ext.compose.ComposeFragmentState
import com.civilcam.domain.model.alerts.UserEmergencyState

data class EmergencyState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val emergencyState: UserEmergencyState = UserEmergencyState.IN_SAFE,
    val emergencyButton: EmergencyButton = EmergencyButton.InSafeButton
) : ComposeFragmentState