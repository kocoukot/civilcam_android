package com.civilcam.ui.alerts.list.model

import com.civilcam.ext_features.compose.ComposeFragmentRoute

sealed class AlertListRoute : ComposeFragmentRoute {
    object GoMyProfile : AlertListRoute()
    object GoSettings : AlertListRoute()
    object GoAlertHistory : AlertListRoute()
    data class GoUserAlert(val userId: Int) : AlertListRoute()
}
