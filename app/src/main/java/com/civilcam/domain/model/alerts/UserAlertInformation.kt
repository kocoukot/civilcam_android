package com.civilcam.domain.model.alerts

data class UserAlertInformation(
    val userId: Int,
    val userName: String,
    val userPhoneNumber: String,
    val requestSent: String
)
