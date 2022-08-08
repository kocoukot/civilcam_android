package com.civilcam.domainLayer.model

enum class VerificationFlow(val rawValue: String) {
    NEW_EMAIL("current_email"),
    NEW_PHONE("phone_verification"),
    CHANGE_EMAIL("new_email"),
    CHANGE_PHONE("CHANGE_PHONE"),
    RESET_PASSWORD("reset_password"),
}