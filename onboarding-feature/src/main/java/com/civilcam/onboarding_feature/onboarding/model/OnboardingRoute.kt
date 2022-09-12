package com.civilcam.onboarding_feature.onboarding.model

import com.civilcam.ext_features.compose.ComposeFragmentRoute

sealed class OnboardingRoute: ComposeFragmentRoute {
    object GoBack : OnboardingRoute()
    object ToCreateAccount : OnboardingRoute()
}
