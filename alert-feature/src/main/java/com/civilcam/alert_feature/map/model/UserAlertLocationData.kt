package com.civilcam.alert_feature.map.model

import com.google.android.gms.maps.model.LatLng

data class UserAlertLocationData(
    val userLocation: LatLng,
    val userBearing: Float,
    val locationData: String = "",
)