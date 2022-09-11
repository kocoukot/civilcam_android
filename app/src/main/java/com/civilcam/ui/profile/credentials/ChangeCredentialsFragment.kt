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
import com.civilcam.common.ext.navigateToRoot
import com.civilcam.domainLayer.model.VerificationFlow
import com.civilcam.ext_features.live_data.observeNonNull
import com.civilcam.ui.common.ext.navController
import com.civilcam.ui.common.ext.requireArg
import com.civilcam.ui.profile.credentials.model.ChangeCredentialsRoute
import com.civilcam.ui.profile.userProfile.model.UserProfileType
import com.civilcam.ui.verification.VerificationFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ChangeCredentialsFragment : Fragment() {
	private val viewModel: ChangeCredentialsViewModel by viewModel {
		parametersOf(credentialType, credential)
	}

	private val credentialType: UserProfileType by requireArg(ARG_CRED_TYPE)
	private val credential: String by requireArg(ARG_CRED)
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		
		viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
			when (route) {
				ChangeCredentialsRoute.GoBack -> navController.popBackStack()
				is ChangeCredentialsRoute.GoSave -> {

					var verificationFlow = VerificationFlow.CHANGE_PHONE
					var mainSubject =
						resources.getString(R.string.verification_phone_mask, route.data)
					var newSubject = ""

					if (route.dataType == UserProfileType.EMAIL) {
						verificationFlow = VerificationFlow.CHANGE_EMAIL
						mainSubject = route.currentEmail
						newSubject = route.data
					}
					navController.navigate(
						R.id.verificationFragment,
						VerificationFragment.createArgs(
							verificationFlow,
							mainSubject,
							newSubject
						)
					)
				}
				ChangeCredentialsRoute.ForceLogout -> navController.navigateToRoot(R.id.onBoardingFragment)
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
		private const val ARG_CRED = "credential"

		fun createArgs(credentialType: UserProfileType, credential: String) = bundleOf(
			ARG_CRED_TYPE to credentialType,
			ARG_CRED to credential
		)
	}
}