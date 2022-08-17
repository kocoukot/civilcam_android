package com.civilcam.domainLayer.model

import androidx.annotation.StringRes
import com.civilcam.domainLayer.R

enum class VerificationFlow(val rawValue: String, @StringRes val title: Int) {
    CURRENT_EMAIL("current_email", R.string.email_verification),
    NEW_EMAIL("new_email", R.string.email_verification),
    NEW_PHONE("phone_verification", R.string.phone_verification),
    CHANGE_EMAIL("change_email", R.string.email_verification),
    CHANGE_PHONE("phone_verification", R.string.phone_verification),
    RESET_PASSWORD("reset_password", R.string.verification_title),
}