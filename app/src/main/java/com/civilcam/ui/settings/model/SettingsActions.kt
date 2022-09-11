package com.civilcam.ui.settings.model

import com.civilcam.domainLayer.model.user.LanguageType
import com.civilcam.domainLayer.model.user.SettingsNotificationType
import com.civilcam.ext_features.compose.ComposeFragmentActions
import com.civilcam.ui.auth.create.model.PasswordInputDataType

sealed class SettingsActions : ComposeFragmentActions {
    object ClickGoBack : SettingsActions()
    data class ClickSection(val section: SettingsType) : SettingsActions()
    data class ClickAlertSwitch(val status: Boolean, val switchType: SettingsNotificationType) :
        SettingsActions()

    data class ClickSaveLanguage(val languageType: LanguageType) : SettingsActions()
    
    object ClickGoSubscription : SettingsActions()
    data class ClickCloseAlertDialog(val isConfirm: Boolean, val isLogOut: Boolean) :
        SettingsActions()

    object ClickSendToSupport : SettingsActions()

    data class EnterContactSupportInfo(
        val issue: String,
        val description: String,
        val email: String
    ) : SettingsActions()

    data class EnterCurrentPassword(val password: String) : SettingsActions()

    object CheckCurrentPassword : SettingsActions()

    data class NewPasswordEntered(
        val type: PasswordInputDataType,
        val meetCriteria: Boolean,
        val password: String
    ) : SettingsActions()


    object SaveNewPassword : SettingsActions()

    object GoSubscriptionManage : SettingsActions()

    object ClearErrorText : SettingsActions()
    object ClickCloseScreenAlert : SettingsActions()

}
