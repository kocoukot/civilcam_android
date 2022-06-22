package com.civilcam.ui.alerts.history.model

import com.civilcam.domain.model.alerts.AlertType

sealed class AlertHistoryActions {
    object ClickGoBack : AlertHistoryActions()
    data class ClickAlertTypeChange(val alertType: AlertType) : AlertHistoryActions()

    data class ClickGoAlertDetails(val alertId: Int) : AlertHistoryActions()


}
