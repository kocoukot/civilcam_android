package com.civilcam.ui.alerts.list.model

import com.civilcam.common.ext.compose.ComposeFragmentState

data class AlertListState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val data: List<com.civilcam.domainLayer.model.alerts.AlertModel>? = null,
    val resolveId: Int? = null,
) : ComposeFragmentState
