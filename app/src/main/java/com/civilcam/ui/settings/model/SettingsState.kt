package com.civilcam.ui.settings.model

import com.civilcam.ext_features.ScreenAlert
import com.civilcam.ext_features.compose.ComposeFragmentState

data class SettingsState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val screenAlert: ScreenAlert? = null,
    val settingsType: SettingsType = SettingsType.MAIN,
    val data: SettingsModel = SettingsModel(),
) : ComposeFragmentState
