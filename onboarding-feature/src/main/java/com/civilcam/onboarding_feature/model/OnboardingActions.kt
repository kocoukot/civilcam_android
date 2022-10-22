package com.civilcam.onboarding_feature.model

sealed class OnboardingActions {
    object ClickGoBack : OnboardingActions()
    object ClickContinue : OnboardingActions()
}
