package com.civilcam.settings_feature.model

import com.civilcam.ext_features.compose.ComposeFragmentRoute


sealed class SettingsRoute : ComposeFragmentRoute {
    object GoBack : SettingsRoute()
    object GoTerms : SettingsRoute()
    object GoStartScreen : SettingsRoute()
    object GoSubManage : SettingsRoute()
    object ForceLogout : SettingsRoute()
}
