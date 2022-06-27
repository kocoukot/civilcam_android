package com.civilcam.ui.verification

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.civilcam.R
import com.civilcam.domain.model.VerificationFlow
import com.civilcam.ui.common.ext.navController
import com.civilcam.ui.common.ext.observeNonNull
import com.civilcam.ui.common.ext.requireArg
import com.civilcam.ui.verification.model.VerificationRoute
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class VerificationFragment : Fragment() {
	private val viewModel: VerificationViewModel by viewModel(){
		parametersOf(verificationFlow, subject)
	}
	
	private val verificationFlow: VerificationFlow by requireArg(ARG_FLOW)
	private val subject: String by requireArg(ARG_SUBJECT)
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
		viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
			when (route) {
				VerificationRoute.GoBack -> { navController.popBackStack() }
				VerificationRoute.ToNextScreen -> {
					when(verificationFlow) {
						VerificationFlow.NEW_EMAIL -> navController.navigate(R.id.termsFragment)
						VerificationFlow.PHONE -> {}
						VerificationFlow.RESET_PASSWORD -> {}
					}
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
				VerificationScreenContent(viewModel)
			}
		}
	}
	
	companion object {
		private const val ARG_FLOW = "verification_flow"
		private const val ARG_SUBJECT = "verification_subject"
		
		fun createArgs(flow: VerificationFlow, subject: String) = bundleOf(
			ARG_FLOW to flow,
			ARG_SUBJECT to subject
		)
	}
}