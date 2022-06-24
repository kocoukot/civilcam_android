package com.civilcam.ui.settings.model

import com.civilcam.common.ext.compose.ComposeFragmentActions

sealed class SettingsActions : ComposeFragmentActions {
    object ClickGoBack : SettingsActions()

    data class ClickSection(val section: SettingsType) : SettingsActions()
    object ClickAlerts : SettingsActions()
    object ClickSubscription : SettingsActions()
    object ClickChangePassword : SettingsActions()
    object ClickLanguage : SettingsActions()
    object ClickContactSupport : SettingsActions()
    object ClickTermsAndPolicy : SettingsActions()
    object ClickLogOut : SettingsActions()
    object ClickDeleteAccount : SettingsActions()


}
