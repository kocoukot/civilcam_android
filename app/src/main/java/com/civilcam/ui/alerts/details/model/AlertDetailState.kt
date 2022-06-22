package com.civilcam.ui.alerts.details.model

import com.civilcam.common.ext.compose.ComposeFragmentState
import com.civilcam.domain.model.alerts.AlertModel

data class AlertDetailState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val data: List<AlertModel>? = null
) : ComposeFragmentState
