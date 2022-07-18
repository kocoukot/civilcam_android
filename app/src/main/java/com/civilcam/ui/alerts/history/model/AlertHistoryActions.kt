package com.civilcam.ui.alerts.history.model

import com.civilcam.common.ext.compose.ComposeFragmentActions
import com.civilcam.domain.model.alerts.AlertType

sealed class AlertHistoryActions : ComposeFragmentActions {
    object ClickGoBack : AlertHistoryActions()
    data class ClickAlertTypeChange(val alertType: AlertType) : AlertHistoryActions()

    data class ClickGoAlertDetails(val alertId: Int) : AlertHistoryActions()

    object ClickGetMockLis : AlertHistoryActions()

}
