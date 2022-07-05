package com.civilcam.ui.profile.userProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.civilcam.R
import com.civilcam.ui.common.ext.navController
import com.civilcam.ui.common.ext.observeNonNull
import com.civilcam.ui.common.ext.requireArg
import com.civilcam.ui.profile.credentials.ChangeCredentialsFragment
import com.civilcam.ui.profile.credentials.model.CredentialType
import com.civilcam.ui.profile.userProfile.model.UserProfileRoute
import com.civilcam.ui.profile.userProfile.model.UserProfileType
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class UserProfileFragment : Fragment() {
	private val viewModel: UserProfileViewModel by viewModel {
		parametersOf(0)
	}
	
	//private val userId by requireArg<Int>(ARG_USER_ID)
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
			when (route) {
				UserProfileRoute.GoBack -> navController.popBackStack()
				is UserProfileRoute.GoCredentials -> {
					when(route.userProfileType) {
						UserProfileType.PHONE_NUMBER -> {
							navController.navigate(
								R.id.changeCredentialsFragment,
								ChangeCredentialsFragment.createArgs(
									CredentialType.PHONE
								)
							)
						}
						UserProfileType.EMAIL -> {
							navController.navigate(
								R.id.changeCredentialsFragment,
								ChangeCredentialsFragment.createArgs(
									CredentialType.EMAIL
								)
							)
						}
						UserProfileType.PIN_CODE -> {}
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
				UserProfileScreenContent(viewModel)
			}
		}
	}
	
	companion object {
		private const val ARG_USER_ID = "user_id"
		
		fun createArgs(userId: Int) = bundleOf(
			ARG_USER_ID to userId
		)
	}
}