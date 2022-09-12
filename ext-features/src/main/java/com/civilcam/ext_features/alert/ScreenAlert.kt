package com.civilcam.ext_features.alert

import androidx.annotation.StringRes
import com.civilcam.ext_features.R


sealed class ScreenAlert(
    @StringRes val text: Int,
    val alertType: AlertDialogTypes = AlertDialogTypes.OK
) {
    object ReportSentAlert : ScreenAlert(R.string.contact_support_alert_sent_text)
    object PasswordChangedAlert : ScreenAlert(R.string.password_changed_alert_text)
    object PinChangedAlert : ScreenAlert(R.string.pincode_changed_alert_text)
}