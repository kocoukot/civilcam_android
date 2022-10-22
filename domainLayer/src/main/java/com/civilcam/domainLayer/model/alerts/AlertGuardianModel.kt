package com.civilcam.domainLayer.model.alerts

import com.civilcam.domainLayer.model.user.ImageInfo

class AlertGuardianModel(
    val id: Int,
    val language: String,
    val fullName: String,
    val avatar: ImageInfo,
    val dob: String,
    val phone: String,
    val address: String,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
)
