package com.civilcam.ui.settings.model

import com.civilcam.common.ext.compose.ComposeFragmentActions
import com.civilcam.domain.model.settings.NotificationsType

sealed class SettingsActions : ComposeFragmentActions {
    object ClickGoBack : SettingsActions()
    data class ClickSection(val section: SettingsType) : SettingsActions()
    data class ClickAlertSwitch(val status: Boolean, val switchType: NotificationsType) :
        SettingsActions()


    object ClickSaveLanguage : SettingsActions()

    data class ClickCloseAlertDialog(val isConfirm: Boolean) : SettingsActions()


    object ClickAlerts : SettingsActions()
    object ClickSubscription : SettingsActions()
    object ClickChangePassword : SettingsActions()
    object ClickLanguage : SettingsActions()
    object ClickContactSupport : SettingsActions()
    object ClickTermsAndPolicy : SettingsActions()
    object ClickLogOut : SettingsActions()
    object ClickDeleteAccount : SettingsActions()


}
