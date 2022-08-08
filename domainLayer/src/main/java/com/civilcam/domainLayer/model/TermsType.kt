package com.civilcam.domainLayer.model

import androidx.annotation.StringRes
import com.civilcam.domainLayer.R

enum class TermsType(@StringRes val title: Int) {
    TERMS_CONDITIONS(R.string.terms_conditions_terms_button),
    PRIVACY_POLICY(R.string.terms_conditions_privacy_button)
}