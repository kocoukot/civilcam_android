package com.civilcam.ui.emergency.model

import com.civilcam.domainLayer.EmergencyScreen
import com.civilcam.domainLayer.model.alerts.AlertDetailModel
import com.civilcam.domainLayer.model.user.ImageInfo
import com.civilcam.ext_features.compose.ComposeFragmentState

data class EmergencyState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val isLocationAllowed: Boolean = false,
    val emergencyScreen: EmergencyScreen = EmergencyScreen.NORMAL,
    val emergencyButton: EmergencyButton = EmergencyButton.InSafeButton,
    val emergencyUserModel: EmergencyUserModel? = null,
    val userAvatar: ImageInfo? = null,
    val alertInfo: AlertDetailModel? = null,
    val isFlashEnabled: Boolean = false,
) : ComposeFragmentState