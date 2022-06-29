package com.civilcam.ui.settings.model

import com.civilcam.common.ext.compose.ComposeFragmentState

data class SettingsState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val settingsType: SettingsType = SettingsType.MAIN,
    val data: SettingsModel = SettingsModel(),
) : ComposeFragmentState
