package com.civilcam.ui.onboarding.model

sealed class OnboardingRoute {
    object GoBack : OnboardingRoute()
    object ToCreateAccount : OnboardingRoute()
}
