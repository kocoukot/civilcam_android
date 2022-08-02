package com.civilcam.ui.alerts.list.model

import com.civilcam.common.ext.compose.ComposeFragmentRoute

sealed class AlertListRoute : ComposeFragmentRoute {
    object GoMyProfile : AlertListRoute()
    object GoSettings : AlertListRoute()
    object GoAlertHistory : AlertListRoute()
    data class GoUserAlert(val userId: Int) : AlertListRoute()
}
