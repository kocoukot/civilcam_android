package com.civilcam.ui.subscription.data

import com.civilcam.R
import com.civilcam.ext_features.alert.AlertDialogButtons
import com.civilcam.ext_features.alert.AlertDialogType

object SubExpired : AlertDialogType.Base(
    title = R.string.subscription_expired_alert_text,
    text = R.string.subscription_expired_alert_text,
)

class SubConfirmAlert(
    private val price: String,
    private val endDate: String,
) : AlertDialogType.Base(
    title = R.string.subscription_confirm_alert_title,
    text = R.string.subscription_confirm_alert_text,
    alertButtons = AlertDialogButtons.CONFIRM_CANCEL
) {
    fun getPrice() = price
    fun getDate() = endDate
}

object SubConfirmed : AlertDialogType.Base(
    title = R.string.subscription_purchase_success_title,
    text = R.string.subscription_purchase_success_description,
)


