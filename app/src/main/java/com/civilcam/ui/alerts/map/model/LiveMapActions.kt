package com.civilcam.ui.alerts.map.model

import com.civilcam.ext_features.compose.ComposeFragmentActions
import com.civilcam.ui.emergency.model.EmergencyScreen

sealed class LiveMapActions : ComposeFragmentActions {
    object ClickGoBack : LiveMapActions()
    object ClickResolveAlert : LiveMapActions()
    object ClickCallPolice : LiveMapActions()
    data class ClickScreenChange(val screenState: EmergencyScreen) : LiveMapActions()
    object ClickDetectLocation : LiveMapActions()
    object ClickCallUserPhone : LiveMapActions()
    data class ClickResolveAlertAnswer(val answer: Boolean) : LiveMapActions()
}