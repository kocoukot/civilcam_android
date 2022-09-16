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
import com.civilcam.ext_features.ext.hideSystemUI
import com.civilcam.ext_features.ext.showSystemUI
import com.civilcam.ext_features.live_data.observeNonNull
import com.civilcam.ext_features.navController
import com.civilcam.ext_features.requireArg
import com.civilcam.ui.subscription.model.SubscriptionRoute
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SubscriptionFragment : Fragment() {
	private val viewModel: SubscriptionViewModel by viewModel {
		parametersOf(isReselect)
	}
	
	private val isReselect: Boolean by requireArg(ARG_FLOW)
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
			when (route) {
				SubscriptionRoute.GoBack -> navController.popBackStack()
				SubscriptionRoute.GoProfileSetup -> navController.navigate(R.id.profileSetupFragment)
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
		
		fun createArgs(isReselect: Boolean) = bundleOf(
			ARG_FLOW to isReselect
		)
	}
}