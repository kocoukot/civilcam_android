package com.civilcam.ui.settings.model

import com.civilcam.domainLayer.model.SubscriptionPlan
import com.civilcam.ui.auth.create.model.PasswordModel

data class SettingsModel(
    val alertsSectionData: SettingsAlertsSectionData = SettingsAlertsSectionData(),
    val contactSupportSectionData: ContactSupportSectionData = ContactSupportSectionData(),
    var changePasswordSectionData: ChangePasswordSectionData? = null,
    var createPasswordSectionData: PasswordModel = PasswordModel(),
    val subscriptionData: SubscriptionPlan = SubscriptionPlan()
)

data class SettingsAlertsSectionData(
    var isSMS: Boolean = false,
    var isEmail: Boolean = false
)


data class ContactSupportSectionData(
    var issueTheme: String = "",
    var issueDescription: String = "",
    val canChangeEmail: Boolean = false,
    var replyEmail: String = ""
)

data class ChangePasswordSectionData(
    val currentPassword: String = "",
    val hasError: Boolean = false
)