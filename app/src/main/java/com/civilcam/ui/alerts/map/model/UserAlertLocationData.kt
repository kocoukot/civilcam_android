package com.civilcam.ui.alerts.map.model

import com.google.android.gms.maps.model.LatLng

data class UserAlertLocationData(
    val userLocation: LatLng,
    val userBearing: Float,
    val locationData: String = "",
)
