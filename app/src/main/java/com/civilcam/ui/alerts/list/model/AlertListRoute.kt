package com.civilcam.ui.alerts.list.model

sealed class AlertListRoute {
    object GoMyProfile : AlertListRoute()
    object GoSettings : AlertListRoute()
    object GoAlertHistory : AlertListRoute()
    data class GoUserProfile(val userId: Int) : AlertListRoute()
}
