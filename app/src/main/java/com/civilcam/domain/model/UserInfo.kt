package com.civilcam.domain.model

data class UserInfo(
    val userId: Int = 0,
    val userName: String = "",
    val dateOfBirth: Long = 0,
    val address: String = "",
    val phoneNumber: String = "",
    val avatar: Int = 0,
)