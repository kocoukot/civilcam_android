package com.civilcam.domainLayer.model.alerts

import com.civilcam.domainLayer.model.UserInfo

data class AlertModel(
    val alertId: Int,
    val userInfo: UserInfo,
    var isResolved: Boolean,
    val alertDate: Long,
)
