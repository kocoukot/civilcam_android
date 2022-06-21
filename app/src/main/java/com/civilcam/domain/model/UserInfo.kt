package com.civilcam.domain.model

data class UserInfo(
    val userName: String = "",
    val dateOfBirth: Long = 0,
    val address: String = "",
    val phoneNumber: String = "",
    val avatar: String = "",
)