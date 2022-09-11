package com.civilcam.ui.alerts.history.model

import com.civilcam.ext_features.compose.ComposeFragmentRoute

sealed class AlertHistoryRoute : ComposeFragmentRoute {
    object GoBack : AlertHistoryRoute()
}
