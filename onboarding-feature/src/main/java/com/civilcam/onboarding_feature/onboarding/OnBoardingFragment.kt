package com.civilcam.onboarding_feature.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.civilcam.ext_features.hideSystemUI
import com.civilcam.ext_features.live_data.observeNonNull
import com.civilcam.ext_features.navController
import com.civilcam.ext_features.showSystemUI
import com.civilcam.onboarding_feature.onboarding.model.OnboardingRoute
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
                OnboardingRoute.ToCreateAccount -> navController.navigate(getString(com.civilcam.ext_features.R.string.direction_createAccountFragment).toUri())
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