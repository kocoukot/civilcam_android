package com.civilcam.settings_feature.model

import com.civilcam.ext_features.alert.ScreenAlert
import com.civilcam.ext_features.compose.ComposeFragmentState

data class SettingsState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val screenAlert: ScreenAlert? = null,
    val settingsType: SettingsType = SettingsType.MAIN,
    val data: SettingsModel = SettingsModel(),
) : ComposeFragmentState
