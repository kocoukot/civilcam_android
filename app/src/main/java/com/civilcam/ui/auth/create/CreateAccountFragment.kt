package com.civilcam.ui.auth.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.civilcam.R
import com.civilcam.domainLayer.model.VerificationFlow
import com.civilcam.ui.auth.create.model.CreateAccountRoute
import com.civilcam.ui.common.ext.navController
import com.civilcam.ui.common.ext.observeNonNull
import com.civilcam.ui.verification.VerificationFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateAccountFragment : Fragment() {
	private val viewModel: CreateAccountViewModel by viewModel()
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
		viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
			when (route) {
				CreateAccountRoute.GoBack -> {
					navController.popBackStack()
				}
				CreateAccountRoute.GoLogin -> {
					navController.navigate(R.id.loginFragment)
				}
				is CreateAccountRoute.GoContinue -> {
					navController.navigate(
						R.id.verificationFragment,
						VerificationFragment.createArgs(
							VerificationFlow.NEW_EMAIL,
							route.email,
							""
						)
					)
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
				CreateAccountScreenContent(viewModel)
			}
		}
	}
}
