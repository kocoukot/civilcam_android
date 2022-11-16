package com.civilcam.domainLayer.model.alerts

import com.civilcam.domainLayer.model.StreamStatusType
import com.civilcam.domainLayer.model.guard.PersonModel

data class AlertModel(
    val alertId: Int,
    val alertDate: String,
    val alertLocation: String,
    val alertUrl: String? = "",
    val alertKey: String? = "",
    var alertStatus: AlertStatus,
    val userInfo: PersonModel?,
    val streamStatus: StreamStatusType? = StreamStatusType.DISABLED
)