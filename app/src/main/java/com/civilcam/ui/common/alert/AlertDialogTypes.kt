package com.civilcam.ui.common.alert

enum class AlertDialogTypes(val negativeText: String, val positiveText: String) {
    CONFIRM_CANCEL("CANCEL", "CONFIRM"),

    ALLOW_DENY("DENY", "ALLOW"),
    ADD_DENY("DENY", "ADD"),
    OK("", "OK"),
    SEND_CANCEL("CANCEL", "SEND"),
    GREAT("", "GREAT!"),
    GOT_IT("", "GOT IT"),
    DELETE_CANCEL("CANCEL", "DELETE"),
    LOG_OUT_CANCEL("CANCEL", "LOG OUT"),
}