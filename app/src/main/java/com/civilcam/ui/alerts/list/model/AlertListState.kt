package com.civilcam.ui.alerts.list.model

import com.civilcam.domain.model.alerts.AlertModel

data class AlertListState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val data: List<AlertModel>? = null,
    val resolveId: Int? = null,
)
