package com.civilcam.ui.alerts.details.model

import com.civilcam.common.ext.compose.ComposeFragmentRoute

sealed class AlertDetailRoute : ComposeFragmentRoute {
    object GoBack : AlertDetailRoute()
}
