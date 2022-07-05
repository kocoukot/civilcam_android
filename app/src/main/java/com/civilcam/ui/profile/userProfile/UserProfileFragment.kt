package com.civilcam.ui.profile.userProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.civilcam.ui.common.ext.observeNonNull
import com.civilcam.ui.common.ext.requireArg
import com.civilcam.ui.profile.userProfile.model.UserProfileScreen
import com.civilcam.ui.profile.userProfile.model.UserProfileType
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class UserProfileFragment : Fragment() {
	private val viewModel: UserProfileViewModel by viewModel {
		parametersOf(userId, profileType)
	}
	
	private val userId by requireArg<Int>(ARG_USER_ID)
	private val profileType by requireArg<UserProfileScreen>(ARG_PROFILE_TYPE)
	
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
				UserProfileScreenState(viewModel)
			}
		}
	}
	
	companion object {
		private const val ARG_USER_ID = "user_id"
		private const val ARG_PROFILE_TYPE = "profile_type"
		
		fun createArgs(userId: Int, profileType: UserProfileScreen) = bundleOf(
			ARG_USER_ID to userId,
			ARG_PROFILE_TYPE to profileType
		)
	}
}