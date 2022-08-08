package com.civilcam.ui.settings.model

import com.civilcam.ui.auth.create.model.PasswordModel

data class SettingsModel(
    val alertsSectionData: SettingsAlertsSectionData? = null,
    val contactSupportSectionData: ContactSupportSectionData? = null,
    var changePasswordSectionData: ChangePasswordSectionData? = null,
    var createPasswordSectionData: PasswordModel = PasswordModel(),
    val subscriptionData: com.civilcam.domainLayer.model.SubscriptionPlan = com.civilcam.domainLayer.model.SubscriptionPlan()
)

data class SettingsAlertsSectionData(
    var isSMS: Boolean,
    var isEmail: Boolean
)


data class ContactSupportSectionData(
    var issueTheme: String,
    var issueDescription: String,
    var replyEmail: String? = null
)

data class ChangePasswordSectionData(
    val currentPassword: String = "",
    val error: String? = ""
)