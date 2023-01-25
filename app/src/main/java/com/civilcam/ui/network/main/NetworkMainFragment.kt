package com.civilcam.ui.network.main

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import com.civilcam.R
import com.civilcam.ext_features.SupportBottomBar
import com.civilcam.ext_features.arch.BaseFragment
import com.civilcam.ext_features.arch.VoiceRecord
import com.civilcam.ext_features.arg
import com.civilcam.ext_features.compose.ComposeFragmentRoute
import com.civilcam.ext_features.ext.setPan
import com.civilcam.ext_features.ext.setResize
import com.civilcam.ext_features.navController
import com.civilcam.ext_features.registerForPermissionsResult
import com.civilcam.ui.MainActivity
import com.civilcam.ui.network.main.model.NetworkMainRoute
import com.civilcam.ui.network.main.model.NetworkScreen
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class NetworkMainFragment : BaseFragment<NetworkMainViewModel>(), SupportBottomBar {
	override val viewModel: NetworkMainViewModel by viewModel {
		parametersOf(screen)
	}
	override val screenContent: @Composable (NetworkMainViewModel) -> Unit = {
		NetworkMainScreenContent(viewModel)
	}

	private val contactsPermissionDelegate = registerForPermissionsResult(
		Manifest.permission.READ_CONTACTS
	) { onContactsPermissionsGranted(it) }
	private var pendingAction: (() -> Unit)? = null

	private val screen: NetworkScreen by arg(ARG_NETWORK_SCREEN, NetworkScreen.MAIN)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		(requireActivity() as VoiceRecord).startVoiceRecord()
		(activity as MainActivity)
			.showBottomNavBar(screen == NetworkScreen.MAIN)
	}

	override fun observeData(composeRoute: ((ComposeFragmentRoute) -> Unit)?) {
		super.observeData { route ->
			when (route) {
				NetworkMainRoute.GoContacts -> checkContactsPermission()
				is NetworkMainRoute.IsNavBarVisible -> (activity as MainActivity)
					.showBottomNavBar(route.isVisible)
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