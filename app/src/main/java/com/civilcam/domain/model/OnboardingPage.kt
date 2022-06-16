package com.civilcam.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class OnboardingPage(
    @StringRes val title: Int,
    @StringRes val text: Int,
    @DrawableRes val image: Int,
)
