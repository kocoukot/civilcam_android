package com.civilcam.ui.settings.model

import com.civilcam.common.ext.compose.ComposeFragmentActions
import com.civilcam.ui.auth.create.model.PasswordInputDataType

sealed class SettingsActions : ComposeFragmentActions {
    object ClickGoBack : SettingsActions()
    data class ClickSection(val section: SettingsType) : SettingsActions()
    data class ClickAlertSwitch(val status: Boolean, val switchType: com.civilcam.domainLayer.model.settings.NotificationsType) :
        SettingsActions()

    object ClickSaveLanguage : SettingsActions()
    
    object ClickGoSubscription : SettingsActions()
    data class ClickCloseAlertDialog(val isConfirm: Boolean, val isLogOut: Boolean) :
        SettingsActions()

    object ClickSendToSupport : SettingsActions()
    data class IsNavBarVisible(val hideNavBar: Boolean) : SettingsActions()

    data class EnterContactSupportInfo(
        val issue: String,
        val description: String,
        val email: String
    ) :
        SettingsActions()

    data class EnterCurrentPassword(val password: String) : SettingsActions()

    object CheckCurrentPassword : SettingsActions()

    data class NewPasswordEntered(
        val type: PasswordInputDataType,
        val meetCriteria: Boolean,
        val password: String
    ) : SettingsActions()


    object SaveNewPassword : SettingsActions()
    
    object GoSubscriptionManage : SettingsActions()

}
