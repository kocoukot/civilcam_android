package com.civilcam.ui.verification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.civilcam.domain.model.VerificationFlow
import com.civilcam.ui.common.ext.observeNonNull
import com.civilcam.ui.common.ext.requireArg
import org.koin.androidx.viewmodel.ext.android.viewModel

class VerificationFragment : Fragment() {
	private val viewModel: VerificationViewModel by viewModel()
	
//	private val verificationFlow: VerificationFlow by requireArg(ARG_FLOW)
//	private val subject: String by requireArg(ARG_SUBJECT)
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
			when (route) {
			
			}
		}
		
		return ComposeView(requireContext()).apply {
			setViewCompositionStrategy(
				ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
					viewLifecycleOwner
				)
			)
			setContent {
				VerificationScreenContent(viewModel, VerificationFlow.NEW_EMAIL, "subject@messapps.com")
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