package com.civilcam.alert_feature.list.model

import com.civilcam.ext_features.compose.ComposeFragmentActions

sealed class AlertListActions : ComposeFragmentActions {
    object ClickGoMyProfile : AlertListActions()
    object ClickGoSettings : AlertListActions()

    data class ClickResolveAlert(val alertId: Int) : AlertListActions()
    data class ClickConfirmResolve(val result: Boolean) : AlertListActions()

    data class ClickGoUserProfile(val alertId: Int) : AlertListActions()

    object ClickGoAlertsHistory : AlertListActions()
    object ClearErrorText : AlertListActions()
    object StopRefresh : AlertListActions()
    data class SetErrorText(val error: String) : AlertListActions()
}
