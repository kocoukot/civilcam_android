package com.civilcam.ui.alerts.history.model

import com.civilcam.common.ext.compose.ComposeFragmentState

data class AlertHistoryState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val alertHistoryScreen: AlertHistoryScreen = AlertHistoryScreen.HISTORY_LIST,
    val alertType: com.civilcam.domainLayer.model.alerts.AlertType = com.civilcam.domainLayer.model.alerts.AlertType.RECEIVED,
    val data: List<com.civilcam.domainLayer.model.alerts.AlertModel>? = null,
    val mockNeedToLoad: Boolean = false
) : ComposeFragmentState
