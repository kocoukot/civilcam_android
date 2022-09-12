package com.civilcam.onboarding_feature.onboarding.model

sealed class OnboardingActions {
    object ClickGoBack : OnboardingActions()
    object ClickContinue : OnboardingActions()
}
