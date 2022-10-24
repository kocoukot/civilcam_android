package com.civilcam.alert_feature.map.model

import com.civilcam.domainLayer.EmergencyScreen
import com.civilcam.ext_features.compose.ComposeFragmentActions

sealed class LiveMapActions : ComposeFragmentActions {
    object ClickGoBack : LiveMapActions()
    object ClickResolveAlert : LiveMapActions()
    object ClickCallPolice : LiveMapActions()
    data class ClickScreenChange(val screenState: EmergencyScreen) : LiveMapActions()
    object ClickCallUserPhone : LiveMapActions()
    data class ClickResolveAlertAnswer(val answer: Boolean) : LiveMapActions()
    data class SelectLocationPermission(val isAllowed: Boolean) : LiveMapActions()
}