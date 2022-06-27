package com.civilcam.ui.settings.model

import androidx.annotation.StringRes
import com.civilcam.R

enum class SettingsType(@StringRes val title: Int) {
    MAIN(R.string.settings_title),

    ALERTS(R.string.settings_alerts),
    SUBSCRIPTION(R.string.settings_subscription),
    CHANGE_PASSWORD(R.string.settings_change_password),
    CREATE_PASSWORD(R.string.settings_change_password),

    LANGUAGE(R.string.settings_language),
    CONTACT_SUPPORT(R.string.settings_contact_support),

    TERMS_AND_POLICY(R.string.settings_terms),

    LOG_OUT(R.string.settings_log_out),
    DELETE_ACCOUNT(R.string.settings_delete_account),
}