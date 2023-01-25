package com.civilcam.ui.emergency.model

sealed class EmergencyRoute {
	object GoUserProfile : EmergencyRoute()
	object GoSettings : EmergencyRoute()
	object GoPinCode : EmergencyRoute()
	object ShowSystemUI : EmergencyRoute()
	object HideSystemUI : EmergencyRoute()

	data class CheckPermission(val isSos: Boolean) : EmergencyRoute()
	data class GoLive(val streamKey: String) : EmergencyRoute()
	data class IsNavBarVisible(val isVisible: Boolean) : EmergencyRoute()
	object SubscriptionEnd : EmergencyRoute()

}