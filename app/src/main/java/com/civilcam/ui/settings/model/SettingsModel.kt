package com.civilcam.ui.settings.model

data class SettingsModel(
    val alertsSectionData: SettingsAlertsSectionData? = null,
    val contactSupportSectionData: ContactSupportSectionData? = null
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