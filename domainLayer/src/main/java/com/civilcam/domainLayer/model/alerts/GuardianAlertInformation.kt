package com.civilcam.domainLayer.model.alerts

import com.google.android.gms.maps.model.LatLng

data class GuardianAlertInformation(
    val userId: Int,
    val userName: String,
    val userPhoneNumber: String,
    val requestSent: String,
    val userAddress: String,
    val userLocation: LatLng
)
