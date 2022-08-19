package com.civilcam.ui.emergency.model

import com.google.android.gms.maps.model.LatLng

data class EmergencyUserModel(
    val userLocation: LatLng,
    val userBearing: Float,
    val locationData: String = "",
    val guardsLocation: List<LatLng> = emptyList()
)
