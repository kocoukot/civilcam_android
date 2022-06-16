package com.civilcam.domain.model

import androidx.annotation.StringRes
import com.civilcam.R

enum class TermsType(@StringRes val title: Int) {
    TERMS_CONDITIONS(R.string.terms_conditions_terms_button),
    PRIVACY_POLICY(R.string.terms_conditions_privacy_button)
}