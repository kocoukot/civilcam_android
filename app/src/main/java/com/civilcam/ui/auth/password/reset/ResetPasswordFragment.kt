package com.civilcam.ui.auth.password.reset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.civilcam.R
import com.civilcam.domainLayer.model.VerificationFlow
import com.civilcam.ext_features.live_data.observeNonNull
import com.civilcam.ext_features.navController
import com.civilcam.ui.auth.password.reset.model.ResetRoute
import com.civilcam.ui.verification.VerificationFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResetPasswordFragment : Fragment() {
	private val viewModel: ResetPasswordViewModel by viewModel()
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
			when (route) {
				ResetRoute.GoBack -> navController.popBackStack()
				is ResetRoute.GoContinue -> navController.navigate(
					R.id.verificationFragment,
					VerificationFragment.createArgs(VerificationFlow.RESET_PASSWORD, route.email, "")
				)
			}
		}
		
		return ComposeView(requireContext()).apply {
			setViewCompositionStrategy(
				ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
					viewLifecycleOwner
				)
			)
			setContent {
				ResetPasswordScreenContent(viewModel)
			}
		}
	}
}