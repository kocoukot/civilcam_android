package com.civilcam.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.civilcam.R
import com.civilcam.common.ext.hideSystemUI
import com.civilcam.common.ext.showSystemUI
import com.civilcam.ui.MainActivity
import com.civilcam.ui.common.ext.navController
import com.civilcam.ui.common.ext.observeNonNull
import com.civilcam.ui.onboarding.model.OnboardingRoute
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
                OnboardingRoute.ToCreateAccount -> navController.navigate(R.id.createAccountFragment)
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
        (activity as MainActivity).hideNotificationBtn()
        hideSystemUI()
    }

    override fun onStop() {
        super.onStop()
        showSystemUI()
    }
}