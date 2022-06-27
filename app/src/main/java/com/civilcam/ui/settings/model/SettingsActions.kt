package com.civilcam.ui.settings.model

import com.civilcam.common.ext.compose.ComposeFragmentActions
import com.civilcam.domain.model.settings.NotificationsType

sealed class SettingsActions : ComposeFragmentActions {
    object ClickGoBack : SettingsActions()
    data class ClickSection(val section: SettingsType) : SettingsActions()
    data class ClickAlertSwitch(val status: Boolean, val switchType: NotificationsType) :
        SettingsActions()

    object ClickSaveLanguage : SettingsActions()
    data class ClickCloseAlertDialog(val isConfirm: Boolean, val isLogOut: Boolean) :
        SettingsActions()

    object ClickSendToSupport : SettingsActions()
    data class IsNavBarVisible(val isVisible: Boolean) : SettingsActions()

    data class EnterContactSupportInfo(
        val issue: String,
        val description: String,
        val email: String
    ) :
        SettingsActions()

    data class EnterCurrentPassword(val password: String) : SettingsActions()

    object CheckCurrentPassword : SettingsActions()

}
