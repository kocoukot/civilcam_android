package com.civilcam.domainLayer.model

import com.civilcam.domainLayer.model.user.UserInfo


data class UserDetailsModel(
    var userInfoSection: UserInfo = UserInfo(),
    var isMyGuard: Boolean = false,
    var guardRequest: GuardRequest? = null,
)

data class GuardRequest(
    val isGuarding: Boolean = false,
)

