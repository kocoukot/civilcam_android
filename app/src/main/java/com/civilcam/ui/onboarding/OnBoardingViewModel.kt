package com.civilcam.ui.onboarding

import androidx.lifecycle.ViewModel
import com.civilcam.ext_features.live_data.SingleLiveEvent
import com.civilcam.ui.onboarding.model.OnboardingActions
import com.civilcam.ui.onboarding.model.OnboardingRoute

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
