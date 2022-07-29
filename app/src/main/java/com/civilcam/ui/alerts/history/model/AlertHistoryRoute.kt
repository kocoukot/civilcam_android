package com.civilcam.ui.alerts.history.model

import com.civilcam.common.ext.compose.ComposeFragmentRoute

sealed class AlertHistoryRoute : ComposeFragmentRoute {
    object GoBack : AlertHistoryRoute()
}
