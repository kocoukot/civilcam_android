package com.civilcam.domainLayer.model

enum class VerificationFlow(val rawValue: String) {
    CURRENT_EMAIL("current_email"),
    NEW_EMAIL("new_email"),
    NEW_PHONE("phone_verification"),
    CHANGE_EMAIL("change_email"),
    CHANGE_PHONE("phone_verification"),
    RESET_PASSWORD("reset_password"),
}