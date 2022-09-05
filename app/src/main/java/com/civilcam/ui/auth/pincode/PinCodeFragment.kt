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
import androidx.fragment.app.setFragmentResult
import com.civilcam.R
import com.civilcam.common.ext.hideKeyboard
import com.civilcam.common.ext.showKeyboard
import com.civilcam.ui.auth.pincode.model.PinCodeFlow
import com.civilcam.ui.auth.pincode.model.PinCodeRoute
import com.civilcam.ui.common.ext.navController
import com.civilcam.ui.common.ext.observeNonNull
import com.civilcam.ui.common.ext.registerForPermissionsResult
import com.civilcam.ui.common.ext.requireArg
import com.civilcam.ui.network.main.NetworkMainFragment
import com.civilcam.ui.network.main.model.NetworkScreen
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PinCodeFragment : Fragment() {
	private val viewModel: PinCodeViewModel by viewModel{
		parametersOf(pinCodeFlow)
	}
	
	private val pinCodeFlow: PinCodeFlow by requireArg(ARG_FLOW)
	
	private val permissionsDelegate = registerForPermissionsResult(
		Manifest.permission.ACCESS_FINE_LOCATION,
		Manifest.permission.ACCESS_COARSE_LOCATION,
		Manifest.permission.CAMERA,
		Manifest.permission.RECORD_AUDIO
	) { onPermissionsGranted(it) }
	
	private var pendingAction: (() -> Unit)? = null
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
			when (route) {
				PinCodeRoute.GoBack -> {
					setFragmentResult(
						RESULT_SAVED_NEW_PIN,
						bundleOf(RESULT_SAVED_NEW_PIN to false)
					)
					navController.popBackStack()
				}
				PinCodeRoute.GoUserProfile -> {
					setFragmentResult(
						RESULT_SAVED_NEW_PIN,
						bundleOf(RESULT_SAVED_NEW_PIN to true)
					)
					navController.popBackStack(R.id.userProfileFragment, false)
				}
				PinCodeRoute.GoGuardians -> checkPermissions()
				PinCodeRoute.GoEmergency -> {
					setFragmentResult(
						RESULT_BACK_STACK,
						bundleOf(RESULT_BACK_STACK to true)
					)
					navController.popBackStack()
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
				PinCodeScreenContent(viewModel)
			}
		}
	}
	
	private fun checkPermissions() {
		if (permissionsDelegate.checkSelfPermissions()) {
			navController.navigate(
				R.id.network_root,
				NetworkMainFragment.createArgs(NetworkScreen.ADD_GUARD)
			)
		} else {
			pendingAction = {
				navController.navigate(
					R.id.network_root,
					NetworkMainFragment.createArgs(NetworkScreen.ADD_GUARD)
                )
            }
            permissionsDelegate.requestPermissions()
        }
    }

    private fun onPermissionsGranted(isGranted: Boolean) {
        pendingAction?.invoke()
        pendingAction = null
    }

    override fun onStart() {
        super.onStart()
        showKeyboard()
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    companion object {
        private const val ARG_FLOW = "pin_code_flow"
		const val RESULT_BACK_STACK = "back_stack"
		const val RESULT_SAVED_NEW_PIN = "saved_new_pin"

        fun createArgs(flow: PinCodeFlow) = bundleOf(
            ARG_FLOW to flow
        )
    }
}