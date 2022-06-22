package com.civilcam.ui.alerts.history.model

sealed class AlertHistoryRoute {
    object GoBack : AlertHistoryRoute()
    data class GoAlertDetails(val alertId: Int) : AlertHistoryRoute()
}
