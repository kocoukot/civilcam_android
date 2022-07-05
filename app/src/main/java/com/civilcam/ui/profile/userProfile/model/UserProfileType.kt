package com.civilcam.ui.profile.userProfile.model

import androidx.annotation.StringRes
import com.civilcam.R

enum class UserProfileType(@StringRes val title: Int) {
	PHONE_NUMBER(R.string.phone_number_text),
	EMAIL(R.string.create_account_email_label),
	PIN_CODE(R.string.user_profile_pin_code_title)
}