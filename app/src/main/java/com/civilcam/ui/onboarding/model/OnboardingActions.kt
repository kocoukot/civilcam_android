package com.civilcam.ui.onboarding.model

sealed class OnboardingActions {
    object CLickGoBack : OnboardingActions()
    object CLickContinue : OnboardingActions()
}
