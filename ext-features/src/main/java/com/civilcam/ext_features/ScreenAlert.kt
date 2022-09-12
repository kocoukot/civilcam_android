package com.civilcam.ext_features

import androidx.annotation.StringRes


sealed class ScreenAlert(
    @StringRes val text: Int,
    val alertType: com.civilcam.ext_features.AlertDialogTypes = com.civilcam.ext_features.AlertDialogTypes.OK
) {
    object ReportSentAlert : ScreenAlert(R.string.contact_support_alert_sent_text)
    object PasswordChangedAlert : ScreenAlert(R.string.password_changed_alert_text)
    object PinChangedAlert : ScreenAlert(R.string.pincode_changed_alert_text)
}