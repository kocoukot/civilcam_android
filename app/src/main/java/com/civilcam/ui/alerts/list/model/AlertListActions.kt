package com.civilcam.ui.alerts.list.model

import com.civilcam.common.ext.compose.ComposeFragmentActions

sealed class AlertListActions : ComposeFragmentActions {
    object ClickGoMyProfile : AlertListActions()
    object ClickGoSettings : AlertListActions()

    data class ClickResolveAlert(val userId: Int) : AlertListActions()
    data class ClickConfirmResolve(val result: Boolean) : AlertListActions()

    data class ClickGoUserProfile(val userId: Int) : AlertListActions()

    object ClickGoAlertsHistory : AlertListActions()

    object ClickGetMockLis : AlertListActions()

}
