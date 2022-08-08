package com.civilcam.domainLayer.model


data class UserDetailsModel(
    var userInfoSection: UserInfo = UserInfo(),
    var isMyGuard: Boolean = false,
    var guardRequest: GuardRequest? = null,
)

data class GuardRequest(
    val isGuarding: Boolean = false,
)

