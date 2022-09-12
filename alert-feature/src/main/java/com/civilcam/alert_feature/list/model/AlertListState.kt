package com.civilcam.alert_feature.list.model

import com.civilcam.domainLayer.model.alerts.AlertModel
import com.civilcam.domainLayer.model.user.ImageInfo
import com.civilcam.ext_features.compose.ComposeFragmentState

data class AlertListState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val data: List<AlertModel>? = null,
    val resolveId: Int? = null,
    val userAvatar: ImageInfo? = null,
) : ComposeFragmentState
