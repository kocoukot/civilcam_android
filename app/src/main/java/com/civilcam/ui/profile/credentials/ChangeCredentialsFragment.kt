package com.civilcam.ui.profile.credentials

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.civilcam.R
import com.civilcam.domain.model.VerificationFlow
import com.civilcam.ui.common.ext.navController
import com.civilcam.ui.common.ext.observeNonNull
import com.civilcam.ui.common.ext.requireArg
import com.civilcam.ui.profile.credentials.model.ChangeCredentialsRoute
import com.civilcam.ui.profile.credentials.model.CredentialType
import com.civilcam.ui.verification.VerificationFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ChangeCredentialsFragment : Fragment() {
	private val viewModel: ChangeCredentialsViewModel by viewModel() {
		parametersOf(credentialType)
	}
	
	private val credentialType: CredentialType by requireArg(ARG_CRED_TYPE)
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		
		viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
			when (route) {
				ChangeCredentialsRoute.GoBack -> navController.popBackStack()
				is ChangeCredentialsRoute.GoSave -> {
					when (route.dataType) {
						CredentialType.PHONE ->
							navController.navigate(
								R.id.verificationFragment,
								VerificationFragment.createArgs(
									VerificationFlow.CHANGE_PHONE,
									resources.getString(
										R.string.verification_phone_mask,
										route.data
									)
								)
							)
						CredentialType.EMAIL ->
							navController.navigate(
								R.id.verificationFragment,
								VerificationFragment.createArgs(
									VerificationFlow.CHANGE_EMAIL,
									route.data
								)
							)
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
				ChangeCredentialsScreenContent(viewModel)
			}
		}
	}
	
	companion object {
		private const val ARG_CRED_TYPE = "credential_type"
		
		fun createArgs(credentialType: CredentialType) = bundleOf(
			ARG_CRED_TYPE to credentialType
		)
	}
}