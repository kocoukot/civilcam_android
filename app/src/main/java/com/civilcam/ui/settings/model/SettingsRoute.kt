package com.civilcam.ui.settings.model

import com.civilcam.ext_features.compose.ComposeFragmentRoute

sealed class SettingsRoute : ComposeFragmentRoute {
    object GoBack : SettingsRoute()
    object GoTerms : SettingsRoute()
    object GoLanguageSelect : SettingsRoute()
    object GoSubManage : SettingsRoute()
    object ForceLogout : SettingsRoute()
}
