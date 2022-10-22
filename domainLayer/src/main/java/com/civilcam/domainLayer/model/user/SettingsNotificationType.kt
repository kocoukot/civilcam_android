package com.civilcam.domainLayer.model.user

enum class SettingsNotificationType(val domain: String) {
    SMS("sms_notification"),
    EMAIL("email_notification"),
    FACE_ID("face_id"),
}