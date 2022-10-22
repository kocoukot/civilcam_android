package com.civilcam.ui.auth.pincode.model

import androidx.annotation.StringRes
import com.civilcam.R

enum class PinCodeFlow(val rawValue: String, @StringRes val title: Int) {
    CREATE_PIN_CODE("CREATE_PIN_CODE", R.string.pin_code_create_title),
    CONFIRM_PIN_CODE("CONFIRM_NEW_PIN_CODE", R.string.pin_code_confirm_title),
    CURRENT_PIN_CODE("CURRENT_PIN_CODE", R.string.change_credentials_pin_code_title),
    NEW_PIN_CODE("NEW_PIN_CODE", R.string.pin_code_new_title),
    CONFIRM_NEW_PIN_CODE("CONFIRM_NEW_PIN_CODE", R.string.pin_code_confirm_new_title),
    SOS_PIN_CODE("SOS_PIN_CODE", R.string.pin_code_enter_title)
}