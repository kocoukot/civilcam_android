package com.civilcam.domainLayer.model.alerts

enum class AlertType(val domain: String) {
    RECEIVED("received"),
    SENT("sent")
}