package com.civilcam.domain.model.alerts

import com.civilcam.domain.model.UserInfo

data class AlertModel(
    val alertId: Int,
    val userInfo: UserInfo,
    var isResolved: Boolean,
    val alertDate: Long,
)
