package com.civilcam.ui.onboarding.model

import com.civilcam.common.ext.compose.ComposeFragmentRoute

sealed class OnboardingRoute: ComposeFragmentRoute {
    object GoBack : OnboardingRoute()
    object ToCreateAccount : OnboardingRoute()
}
