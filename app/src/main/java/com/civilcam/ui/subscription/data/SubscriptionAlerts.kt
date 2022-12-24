package com.civilcam.ui.subscription.data

import com.civilcam.CivilcamApplication.Companion.instance
import com.civilcam.R
import com.civilcam.ext_features.alert.AlertDialogButtons
import com.civilcam.ext_features.alert.AlertDialogType

object SubExpired : AlertDialogType.Base(
    title = instance.getString(R.string.subscription_expired_alert_text),
    text = instance.getString(R.string.subscription_expired_alert_text),
)

class SubConfirmAlert(
    price: String,
    endDate: String,
) : AlertDialogType.Base(
    title = instance.getString(R.string.subscription_confirm_alert_title),
    text = instance.getString(R.string.subscription_confirm_alert_text, price, endDate),
    alertButtons = AlertDialogButtons.CONFIRM_CANCEL
)

object SubConfirmed : AlertDialogType.Base(
    title = instance.getString(R.string.subscription_purchase_success_title),
    text = instance.getString(R.string.subscription_purchase_success_description),
)


