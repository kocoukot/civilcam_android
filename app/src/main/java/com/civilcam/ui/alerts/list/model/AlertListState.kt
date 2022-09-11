package com.civilcam.ui.alerts.list.model

import com.civilcam.domainLayer.model.alerts.AlertModel
import com.civilcam.ext_features.compose.ComposeFragmentState

data class AlertListState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val data: List<AlertModel>? = null,
    val resolveId: Int? = null,
) : ComposeFragmentState
