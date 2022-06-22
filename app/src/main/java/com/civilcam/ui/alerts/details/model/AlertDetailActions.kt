package com.civilcam.ui.alerts.details.model

import com.civilcam.common.ext.compose.ComposeFragmentActions

sealed class AlertDetailActions : ComposeFragmentActions {
    object ClickGoBack : AlertDetailActions()
    object ClickCallPhone : AlertDetailActions()
    object ClickDownloadVideo : AlertDetailActions()
}
