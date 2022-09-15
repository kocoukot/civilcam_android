package com.civilcam.domainLayer.model.alerts

import com.civilcam.domainLayer.model.guard.PersonModel

data class AlertDetailModel(
    val alertModel: AlertModel,
    val alertResolvers: AlertResolver

) {
    class AlertResolver(
        val resolveId: Int,
        val resolveDate: String,
        val userInfo: PersonModel,
    )
}