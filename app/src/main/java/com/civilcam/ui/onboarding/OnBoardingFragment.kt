package com.civilcam.ui.onboarding

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.ViewGroup
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.civilcam.R
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
                OnboardingRoute.GoBack -> {
                    navController.popBackStack()
                }
                OnboardingRoute.ToCreateAccount -> {
                    navController.navigate(R.id.createAccountFragment)
                }
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
        activity?.window?.apply {
            statusBarColor = Color.TRANSPARENT
            if (Build.VERSION.SDK_INT in 21..29) {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                decorView.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            } else if (Build.VERSION.SDK_INT >= 30) {
                WindowCompat.setDecorFitsSystemWindows(this, false)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        activity?.window?.apply {
            if (Build.VERSION.SDK_INT in 21..29) {
                statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else if (Build.VERSION.SDK_INT >= 30) {
                WindowCompat.setDecorFitsSystemWindows(activity!!.window!!, true)
            }
        }
    }
}