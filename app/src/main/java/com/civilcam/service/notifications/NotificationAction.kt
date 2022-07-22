package com.civilcam.service.notifications

enum class NotificationAction(val description: String) {
    CLOSE_ALERT("close_alert"),
    CLOSE_REQUEST("close_request"),
    DENY("deny"),
    ACCEPT("accept"),
    DETAIL("detail");

    companion object {
        fun byDescription(description: String) =
            values().find { it.description.equals(description, true) }
    }
}


