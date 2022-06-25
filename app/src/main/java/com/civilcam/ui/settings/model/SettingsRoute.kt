package com.civilcam.ui.settings.model

import com.civilcam.common.ext.compose.ComposeFragmentRoute

sealed class SettingsRoute : ComposeFragmentRoute {
    object GoBack : SettingsRoute()
    object GoTerms : SettingsRoute()
    object GoLanguageSelect : SettingsRoute()

}
