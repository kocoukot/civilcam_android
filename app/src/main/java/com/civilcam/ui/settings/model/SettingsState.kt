package com.civilcam.ui.settings.model

import com.civilcam.common.ext.compose.ComposeFragmentState
import com.civilcam.domainLayer.model.ScreenAlert

data class SettingsState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val screenAlert: ScreenAlert? = null,
    val settingsType: SettingsType = SettingsType.MAIN,
    var data: SettingsModel = SettingsModel(),
) : ComposeFragmentState
