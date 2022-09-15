package com.civilcam.alert_feature.list.model

import com.civilcam.ext_features.compose.ComposeFragmentRoute

sealed class AlertListRoute : ComposeFragmentRoute {
    object GoMyProfile : AlertListRoute()
    object GoSettings : AlertListRoute()
    object GoAlertHistory : AlertListRoute()
    data class GoUserAlert(val alertId: Int) : AlertListRoute()
}
