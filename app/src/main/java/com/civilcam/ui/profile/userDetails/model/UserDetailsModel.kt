package com.civilcam.ui.profile.userDetails.model

import com.civilcam.domain.model.UserInfo

data class UserDetailsModel(
    val userInfoSection: UserInfo = UserInfo(),
    var isMyGuard: Boolean = false,
    var guardRequest: GuardRequest? = null,
)

data class GuardRequest(
    val isGuarding: Boolean = false,
)

