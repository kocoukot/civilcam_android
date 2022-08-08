package com.civilcam.ui.alerts.history.model

import com.civilcam.common.ext.compose.ComposeFragmentActions

sealed class AlertHistoryActions : ComposeFragmentActions {
    object ClickGoBack : AlertHistoryActions()
    data class ClickAlertTypeChange(val alertType: com.civilcam.domainLayer.model.alerts.AlertType) : AlertHistoryActions()

    data class ClickGoAlertDetails(val alertId: Int) : AlertHistoryActions()

    object ClickGetMockLis : AlertHistoryActions()

    object CLickCallUser : AlertHistoryActions()
    object CLickUploadVideo : AlertHistoryActions()

}
