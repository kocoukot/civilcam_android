package com.civilcam.settings_feature.model

import androidx.annotation.StringRes
import com.civilcam.settings_feature.R

enum class SettingsType(@StringRes val rowTitle: Int, @StringRes val actionBtnTitle: Int) {
    MAIN(R.string.settings_title, com.civilcam.ext_features.R.string.empty_string),

    ALERTS(
        com.civilcam.domainLayer.R.string.alerts_root_list_title,
        com.civilcam.ext_features.R.string.empty_string
    ),
    SUBSCRIPTION(R.string.settings_subscription, com.civilcam.ext_features.R.string.empty_string),
    CHANGE_PASSWORD(
        R.string.settings_change_password,
        com.civilcam.ext_features.R.string.continue_text
    ),
    CREATE_PASSWORD(
        R.string.settings_change_password,
        com.civilcam.ext_features.R.string.save_text
    ),

    LANGUAGE(R.string.settings_language, com.civilcam.ext_features.R.string.save_text),
    CONTACT_SUPPORT(R.string.settings_contact_support, R.string.send_text),

    TERMS_AND_POLICY(R.string.settings_terms, com.civilcam.ext_features.R.string.empty_string),

    LOG_OUT(R.string.settings_log_out, com.civilcam.ext_features.R.string.empty_string),
    DELETE_ACCOUNT(
        R.string.settings_delete_account,
        com.civilcam.ext_features.R.string.empty_string
    );

    companion object {
        fun SettingsType.hasActionButton() =
            this == LANGUAGE || this == CREATE_PASSWORD || this == CONTACT_SUPPORT || this == CHANGE_PASSWORD
    }
}