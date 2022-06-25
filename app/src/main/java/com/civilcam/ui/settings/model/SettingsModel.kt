package com.civilcam.ui.settings.model

data class SettingsModel(
    val alertsSectionData: SettingsAlertsSectionData
)

data class SettingsAlertsSectionData(
    var isSMS: Boolean,
    var isEmail: Boolean
)
