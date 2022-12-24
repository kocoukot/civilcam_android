package com.civilcam.domainLayer.model

import com.civilcam.domainLayer.model.alerts.AlertGuardianModel

data class OnGuardUserData(
    val id: Int,
    val date: String,
    val location: String,
    val person: AlertGuardianModel,
    val status: AlertSocketStatus,
    val url: String?,
    val streamStatus: AlertStreamStatus
) {
    enum class AlertSocketStatus {
        active, resolved, deleted,
    }

    enum class AlertStreamStatus {
        active, idle, disabled,
    }
}