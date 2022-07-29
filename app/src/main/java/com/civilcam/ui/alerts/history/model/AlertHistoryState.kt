package com.civilcam.ui.alerts.history.model

import com.civilcam.common.ext.compose.ComposeFragmentState
import com.civilcam.domain.model.alerts.AlertModel
import com.civilcam.domain.model.alerts.AlertType

data class AlertHistoryState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val alertHistoryScreen: AlertHistoryScreen = AlertHistoryScreen.HISTORY_LIST,
    val alertType: AlertType = AlertType.RECEIVED,
    val data: List<AlertModel>? = null,
    val mockNeedToLoad: Boolean = false
) : ComposeFragmentState
