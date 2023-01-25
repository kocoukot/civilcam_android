package com.civilcam.ui.network.main

import android.Manifest
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
import com.civilcam.ext_features.SupportBottomBar
import com.civilcam.ext_features.arch.VoiceRecord
import com.civilcam.ext_features.arg
import com.civilcam.ext_features.ext.setPan
import com.civilcam.ext_features.ext.setResize
import com.civilcam.ext_features.live_data.observeNonNull
import com.civilcam.ext_features.navController
import com.civilcam.ext_features.registerForPermissionsResult
import com.civilcam.ui.MainActivity
import com.civilcam.ui.network.main.model.NetworkMainRoute
import com.civilcam.ui.network.main.model.NetworkScreen
import com.civilcam.ui.profile.userDetails.UserDetailsFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class NetworkMainFragment : Fragment(), SupportBottomBar {
    private val viewModel: NetworkMainViewModel by viewModel {
        parametersOf(screen)
    }

    private val contactsPermissionDelegate = registerForPermissionsResult(
        Manifest.permission.READ_CONTACTS
    ) { onContactsPermissionsGranted(it) }
    private var pendingAction: (() -> Unit)? = null

    private val screen: NetworkScreen by arg(ARG_NETWORK_SCREEN, NetworkScreen.MAIN)

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		(requireActivity() as VoiceRecord).startVoiceRecord()
		(activity as MainActivity)
			.showBottomNavBar(screen == NetworkScreen.MAIN)
		viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
			when (route) {
				NetworkMainRoute.GoSettings -> navController.navigate(R.id.action_network_root_to_settingsFragment)
				NetworkMainRoute.GoContacts -> checkContactsPermission()
				NetworkMainRoute.GoProfile -> navController.navigate(R.id.userProfileFragment)
				is NetworkMainRoute.GoUserDetail -> navController.navigate(
					R.id.action_network_root_to_userDetailsFragment,
					UserDetailsFragment.createArgs(route.userId)
				)
				is NetworkMainRoute.IsNavBarVisible -> (activity as MainActivity)
					.showBottomNavBar(route.isVisible)
				NetworkMainRoute.ForceLogout -> navController.navigateToRoot(R.id.onBoardingFragment)
			}
		}
		return ComposeView(requireContext()).apply {
			setViewCompositionStrategy(
				ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
					viewLifecycleOwner
				)
			)
			
			setContent {
				NetworkMainScreenContent(viewModel)
			}
		}
	}
	
	override fun onResume() {
		super.onResume()
        viewModel.loadAvatar()
        viewModel.checkNavBarStatus()
		viewModel.loadRequestsList()
		viewModel.getAndMatchContacts()
		setPan()
	}
	
	override fun onStop() {
		super.onStop()
		setResize()
	}
	
	
	private fun checkContactsPermission() {
		if (contactsPermissionDelegate.checkSelfPermissions()) {
			navController.navigate(R.id.action_network_root_to_contactsFragment)
		} else {
			pendingAction = { navController.navigate(R.id.action_network_root_to_contactsFragment) }
			contactsPermissionDelegate.requestPermissions()
		}
	}
	
	
	private fun onContactsPermissionsGranted(isGranted: Boolean) {
		if (isGranted) {
			pendingAction?.invoke()
			pendingAction = null
		}
	}

    companion object {
		private const val ARG_NETWORK_SCREEN = "network_screen"

        fun createArgs(screen: NetworkScreen = NetworkScreen.MAIN) = bundleOf(
			ARG_NETWORK_SCREEN to screen
		)
	}
}