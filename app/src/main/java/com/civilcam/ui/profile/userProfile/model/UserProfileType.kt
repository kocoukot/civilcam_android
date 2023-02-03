package com.civilcam.ui.profile.userProfile.model

import androidx.annotation.StringRes
import com.civilcam.R

enum class UserProfileType(@StringRes val title: Int) {
	PHONE_NUMBER(com.civilcam.ext_features.R.string.profile_setup_phone_number_label),
    EMAIL(R.string.create_account_email_label),
	PIN_CODE(R.string.user_profile_pin_code_title)
}