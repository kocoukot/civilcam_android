package com.civilcam.alert_feature.history.model

import com.civilcam.domainLayer.model.alerts.AlertModel
import com.civilcam.domainLayer.model.alerts.AlertType
import com.civilcam.ext_features.compose.ComposeFragmentState

data class AlertHistoryState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val alertHistoryScreen: AlertHistoryScreen = AlertHistoryScreen.HISTORY_LIST,
    val alertType: AlertType = AlertType.RECEIVED,
    val data: List<AlertModel>? = null,
    val mockNeedToLoad: Boolean = false
) : ComposeFragmentState
