package com.civilcam.domainLayer.model.alerts

enum class AlertStatus(val domain: String) {
    ACTIVE("active"),
    RESOLVED("resolved"),
    DELETED("deleted")

}