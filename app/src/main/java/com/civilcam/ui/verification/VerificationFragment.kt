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
import com.civilcam.ui.auth.password.create.CreatePasswordFragment
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
		parametersOf(verificationFlow, subject, newSubject)
	}

	private val verificationFlow: VerificationFlow by requireArg(ARG_FLOW)
	private val subject: String by requireArg(ARG_SUBJECT)
	private val newSubject: String by requireArg(ARG_NEW_SUBJECT)

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
				is VerificationRoute.GoPasswordCreate -> {
					navController.navigate(
						R.id.createPasswordFragment,
						CreatePasswordFragment.createArgs(route.token)
					)
				}
				VerificationRoute.ToNextScreen -> {
					Handler(Looper.getMainLooper()).postDelayed(300) {
						when (verificationFlow) {
                            VerificationFlow.CURRENT_EMAIL ->
                                navController.navigate(
                                    R.id.termsFragment,
                                    TermsFragment.createArgs()
                                )
                            VerificationFlow.NEW_PHONE -> navController.navigate(
                                R.id.pinCodeFragment,
                                PinCodeFragment.createArgs(PinCodeFlow.CREATE_PIN_CODE, false)
                            )
							VerificationFlow.CHANGE_PHONE -> {
								setFragmentResult(
									RESULT_BACK_PHONE,
									bundleOf(RESULT_BACK_PHONE to true)
								)
								navController.popBackStack(
									R.id.userProfileFragment,
									false
								)
							}
                            VerificationFlow.CHANGE_EMAIL -> {
                                navController.navigate(
                                    R.id.verificationFragment,
                                    createArgs(
                                        VerificationFlow.NEW_EMAIL,
                                        newSubject,
                                        newSubject
                                    )
                                )
                            }
                            VerificationFlow.NEW_EMAIL -> {
                                setFragmentResult(
	                                RESULT_BACK_EMAIL,
                                    bundleOf(RESULT_BACK_EMAIL to true)
                                )
                                navController.popBackStack(
                                    R.id.userProfileFragment,
                                    false
                                )
                            }
							else -> {}
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
		private const val ARG_NEW_SUBJECT = "new_subject"
		const val RESULT_BACK_PHONE = "back_phone"
	    const val RESULT_BACK_EMAIL = "back_email"

        fun createArgs(flow: VerificationFlow, subject: String, newSubject: String) = bundleOf(
			ARG_FLOW to flow,
			ARG_SUBJECT to subject,
			ARG_NEW_SUBJECT to newSubject
		)
	}
}