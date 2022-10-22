package com.civilcam.ui.emergency.model

import com.civilcam.domainLayer.model.alerts.AlertGuardianModel
import com.google.android.gms.maps.model.LatLng

data class EmergencyUserModel(
    val userLocation: LatLng,
    val userBearing: Float,
    val locationData: String = "",
    val guardsLocation: List<AlertGuardianModel> = emptyList()
)
