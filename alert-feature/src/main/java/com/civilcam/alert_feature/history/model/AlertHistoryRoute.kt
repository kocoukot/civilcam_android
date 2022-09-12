package com.civilcam.alert_feature.history.model

import com.civilcam.ext_features.compose.ComposeFragmentRoute

sealed class AlertHistoryRoute : ComposeFragmentRoute {
    object GoBack : AlertHistoryRoute()
}
