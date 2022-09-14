package com.civilcam.onboarding_feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.civilcam.ext_features.ext.hideSystemUI
import com.civilcam.ext_features.ext.navController
import com.civilcam.ext_features.ext.navigateTo
import com.civilcam.ext_features.ext.showSystemUI
import com.civilcam.ext_features.live_data.observeNonNull
import com.civilcam.onboarding_feature.model.OnboardingRoute
import org.koin.androidx.viewmodel.ext.android.viewModel


class OnBoardingFragment : Fragment() {
    private val viewModel: OnBoardingViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
            when (route) {
                OnboardingRoute.GoBack -> navController.popBackStack()
                OnboardingRoute.ToCreateAccount -> navigateTo(getString(com.civilcam.ext_features.R.string.direction_createAccountFragment))
            }
        }

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                    viewLifecycleOwner
                )
            )
            setContent {
                OnBoardingScreenContent(viewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
    }

    override fun onStop() {
        super.onStop()
        showSystemUI()
    }
}