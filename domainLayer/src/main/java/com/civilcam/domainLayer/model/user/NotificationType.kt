package com.civilcam.domainLayer.model.user

enum class NotificationType(var notifyName: String) {
    REQUESTS("Request"),
    ALERTS("Alerts"),
    LOCATION("Location"),
    DOWNLOAD("Download"),

    COMMON("Common"),

}