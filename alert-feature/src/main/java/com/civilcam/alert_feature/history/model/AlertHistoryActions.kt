package com.civilcam.alert_feature.history.model

import com.civilcam.domainLayer.model.alerts.AlertType
import com.civilcam.ext_features.compose.ComposeFragmentActions

sealed class AlertHistoryActions : ComposeFragmentActions {
    object ClickGoBack : AlertHistoryActions()
    data class ClickAlertTypeChange(val alertType: AlertType) : AlertHistoryActions()
    data class ClickGoAlertDetails(val alertId: Int) : AlertHistoryActions()
    object CLickCallUser : AlertHistoryActions()
    object ClickDownloadVideo : AlertHistoryActions()

    object StopRefresh : AlertHistoryActions()

    object ClearErrorText : AlertHistoryActions()
}