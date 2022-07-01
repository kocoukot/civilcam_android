package com.civilcam.ui.auth.pincode

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
import com.civilcam.common.ext.showAlertDialogFragment
import com.civilcam.ui.auth.pincode.model.PinCodeRoute
import com.civilcam.ui.common.ext.navController
import com.civilcam.ui.common.ext.observeNonNull
import com.civilcam.ui.common.ext.registerForPermissionsResult
import com.civilcam.ui.common.ext.requireArg
import com.civilcam.utils.contract.GalleryActivityResultContract
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class PinCodeFragment : Fragment() {
	private val viewModel: PinCodeViewModel by viewModel() {
		parametersOf(false)
	}
	
	private val permissionsDelegate = registerForPermissionsResult(
		Manifest.permission.ACCESS_FINE_LOCATION,
		Manifest.permission.ACCESS_COARSE_LOCATION,
		Manifest.permission.CAMERA,
		Manifest.permission.RECORD_AUDIO
	) { onPermissionsGranted(it) }
	
	private var pendingAction: (() -> Unit)? = null
	
	//private val isConfirm: Boolean by requireArg(ARG_CONFIRM)
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
			when (route) {
				PinCodeRoute.GoBack -> navController.popBackStack()
				PinCodeRoute.GoConfirm -> navController.navigate(R.id.pinCodeFragment, createArgs(true))
				PinCodeRoute.GoGuardians -> permissionsDelegate.requestPermissions()
			}
		}
		
		return ComposeView(requireContext()).apply {
			setViewCompositionStrategy(
				ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
					viewLifecycleOwner
				)
			)
			setContent {
				PinCodeScreenContent(viewModel)
			}
		}
	}
	
	private fun onPermissionsGranted(isGranted: Boolean) {
		if (isGranted) {
			pendingAction?.invoke()
			pendingAction = null
		} else {
			showAlertDialogFragment(title = "Sorry, we need permission!")
		}
	}
	
	companion object {
		private const val ARG_CONFIRM = "confirm"
		
		fun createArgs(isConfirm: Boolean) = bundleOf(
			ARG_CONFIRM to isConfirm
		)
	}
}