package com.civilcam.ui.emergency.model

import com.civilcam.domainLayer.EmergencyScreen
import com.civilcam.domainLayer.model.alerts.AlertInfoModel
import com.civilcam.domainLayer.model.user.ImageInfo

data class EmergencyState(
	val isLoading: Boolean = false,
	val errorText: String = "",
	val isLocationAllowed: Boolean = false,
	val emergencyScreen: EmergencyScreen = EmergencyScreen.NORMAL,
	val emergencyButton: EmergencyButton = EmergencyButton.InSafeButton,
	val emergencyUserModel: EmergencyUserModel? = null,
	val userAvatar: ImageInfo? = null,
	val alertInfo: AlertInfoModel? = null,
	val isFlashEnabled: Boolean = false,
	val toastProgress: Int = 0,
	val toastActivated: Boolean = false
)