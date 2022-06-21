package com.civilcam.ui.onboarding.model

sealed class OnboardingActions {
    object ClickGoBack : OnboardingActions()
    object ClickContinue : OnboardingActions()
}
