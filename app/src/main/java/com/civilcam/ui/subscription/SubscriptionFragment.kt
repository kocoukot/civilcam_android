package com.civilcam.ui.subscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.civilcam.R
import com.civilcam.common.ext.navigateToRoot
import com.civilcam.domainLayer.model.subscription.UserSubscriptionState
import com.civilcam.ext_features.ext.hideSystemUI
import com.civilcam.ext_features.ext.navigateToStart
import com.civilcam.ext_features.ext.showSystemUI
import com.civilcam.ext_features.live_data.observeNonNull
import com.civilcam.ext_features.navController
import com.civilcam.ext_features.requireArg
import com.civilcam.ui.subscription.model.SubscriptionRoute
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SubscriptionFragment : Fragment() {
	private val viewModel: SubscriptionViewModel by viewModel {
		parametersOf(useSubState)
	}

	private val useSubState: UserSubscriptionState by requireArg(ARG_FLOW)

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
			when (route) {
				SubscriptionRoute.GoBack -> navController.popBackStack()
				SubscriptionRoute.GoProfileSetup -> navController.navigate(R.id.profileSetupFragment)
				SubscriptionRoute.GoMap -> navController.navigateToRoot(R.id.emergency_root)
				SubscriptionRoute.GoCreateAccount -> navigateToStart()
			}
		}
		return ComposeView(requireContext()).apply {
			setViewCompositionStrategy(
				ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
					viewLifecycleOwner
				)
			)
			setContent {
				SubscriptionScreenContent(viewModel)
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

	companion object {
		private const val ARG_FLOW = "subscription_flow"
		const val IN_APP_TRIAL = "in_app_trial"

		fun createArgs(isReselect: UserSubscriptionState) = bundleOf(
			ARG_FLOW to isReselect
		)
	}
}