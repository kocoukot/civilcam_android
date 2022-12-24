package com.civilcam.ext_features.alert

import androidx.annotation.StringRes
import com.civilcam.ext_features.R

enum class AlertDialogButtons(@StringRes val negativeText: Int, @StringRes val positiveText: Int) {
    CONFIRM_CANCEL(R.string.cancel_text_caps, R.string.confirm_text_caps),
    YES_CANCEL(R.string.cancel_text_caps, R.string.yes_text_caps),
    OK(0, R.string.ok_text_caps),
    CANCEL_RESOLVE(R.string.cancel_text_caps, R.string.resolve_text_caps),


//    ALLOW_DENY("DENY", "ALLOW"),
//    ADD_DENY("DENY", "ADD"),
//    SEND_CANCEL("CANCEL", "SEND"),
//    GREAT("", "GREAT!"),
//    GOT_IT("", "GOT IT"),
//    DELETE_CANCEL("CANCEL", "DELETE"),
//    LOG_OUT_CANCEL("CANCEL", "LOG OUT"),
}