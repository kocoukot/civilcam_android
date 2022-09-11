package com.civilcam.onboarding_feature.onboarding

import androidx.lifecycle.ViewModel
import com.civilcam.ext_features.live_data.SingleLiveEvent
import com.civilcam.onboarding_feature.onboarding.model.OnboardingActions
import com.civilcam.onboarding_feature.onboarding.model.OnboardingRoute

class OnBoardingViewModel : ViewModel() {

    private val _steps: SingleLiveEvent<OnboardingRoute> = SingleLiveEvent()
    val steps: SingleLiveEvent<OnboardingRoute> = _steps

    fun setInputActions(action: OnboardingActions) {
        when (action) {
            OnboardingActions.ClickContinue -> onContinueClick()
            OnboardingActions.ClickGoBack -> goBack()
        }
    }

    private fun goBack() {
        _steps.value = OnboardingRoute.GoBack
    }

    private fun onContinueClick() {
        _steps.value = OnboardingRoute.ToCreateAccount
    }
}
