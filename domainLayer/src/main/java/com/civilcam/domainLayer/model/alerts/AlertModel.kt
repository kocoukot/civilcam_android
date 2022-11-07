package com.civilcam.domainLayer.model.alerts

import com.civilcam.domainLayer.model.guard.PersonModel

data class AlertModel(
    val alertId: Int,
    val alertDate: String,
    val alertLocation: String,
    val alertUrl: String?,
    var alertStatus: AlertStatus,
    val userInfo: PersonModel?,
)