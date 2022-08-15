package com.civilcam.ui.verification

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.core.os.postDelayed
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.civilcam.R
import com.civilcam.common.ext.hideKeyboard
import com.civilcam.common.ext.showKeyboard
import com.civilcam.domainLayer.model.VerificationFlow
import com.civilcam.ui.auth.pincode.PinCodeFragment
import com.civilcam.ui.auth.pincode.model.PinCodeFlow
import com.civilcam.ui.common.ext.navController
import com.civilcam.ui.common.ext.observeNonNull
import com.civilcam.ui.common.ext.requireArg
import com.civilcam.ui.terms.TermsFragment
import com.civilcam.ui.verification.model.VerificationRoute
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class VerificationFragment : Fragment() {
	private val viewModel: VerificationViewModel by viewModel {
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
				VerificationRoute.GoBack -> {
					navController.popBackStack()
				}
				VerificationRoute.ToNextScreen -> {
					Handler(Looper.getMainLooper()).postDelayed(300) {
						when (verificationFlow) {
							VerificationFlow.NEW_EMAIL -> navController.navigate(
								R.id.termsFragment,
								TermsFragment.createArgs(false)
							)
							VerificationFlow.NEW_PHONE -> navController.navigate(
								R.id.pinCodeFragment,
								PinCodeFragment.createArgs(PinCodeFlow.CREATE_PIN_CODE)
							)
							VerificationFlow.RESET_PASSWORD -> navController.navigate(
								R.id.createPasswordFragment
							)
							VerificationFlow.CHANGE_PHONE -> {
								setFragmentResult(
									RESULT_BACK_STACK,
									bundleOf(RESULT_BACK_STACK to true)
								)
								navController.popBackStack(
									R.id.userProfileFragment,
									false
								)
							}
							VerificationFlow.CHANGE_EMAIL -> {
								setFragmentResult(
									RESULT_BACK_STACK,
									bundleOf(RESULT_BACK_STACK to true)
								)
								navController.popBackStack(
									R.id.userProfileFragment,
									false
								)
							}
						}
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

	override fun onStart() {
		super.onStart()
		showKeyboard()
	}

	override fun onStop() {
		super.onStop()
		hideKeyboard()
	}

	companion object {
		private const val ARG_FLOW = "verification_flow"
		private const val ARG_SUBJECT = "verification_subject"
		const val RESULT_BACK_STACK = "back_stack"

		fun createArgs(flow: VerificationFlow, subject: String) = bundleOf(
			ARG_FLOW to flow,
			ARG_SUBJECT to subject
		)
	}
}