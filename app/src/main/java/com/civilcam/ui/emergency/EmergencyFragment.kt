package com.civilcam.ui.emergency

import android.Manifest
import android.content.Context.CAMERA_SERVICE
import android.content.pm.PackageManager
import android.hardware.Camera
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.civilcam.R
import com.civilcam.common.ext.hideSystemUI
import com.civilcam.common.ext.showSystemUI
import com.civilcam.ui.auth.pincode.PinCodeFragment
import com.civilcam.ui.auth.pincode.model.PinCodeFlow
import com.civilcam.ui.common.SupportBottomBar
import com.civilcam.ui.common.ext.navController
import com.civilcam.ui.common.ext.observeNonNull
import com.civilcam.ui.common.ext.registerForPermissionsResult
import com.civilcam.ui.emergency.model.EmergencyRoute
import org.koin.androidx.viewmodel.ext.android.viewModel


class EmergencyFragment : Fragment(), SupportBottomBar {
	private val viewModel: EmergencyViewModel by viewModel()
	
	private val permissionsDelegate = registerForPermissionsResult(
		Manifest.permission.ACCESS_FINE_LOCATION,
		Manifest.permission.ACCESS_COARSE_LOCATION,
		Manifest.permission.CAMERA,
		Manifest.permission.RECORD_AUDIO
	) { onPermissionsGranted(it) }
	
	private var pendingAction: (() -> Unit)? = null
	
	private var cameraManager: CameraManager? = null
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		setFragmentResultListener(PinCodeFragment.RESULT_BACK_STACK) { _, _ ->
			viewModel.disableSosStatus()
		}
		
		cameraManager = activity?.getSystemService(CAMERA_SERVICE) as CameraManager
		
		viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
			when (route) {
				EmergencyRoute.GoUserProfile -> navController.navigate(R.id.userProfileFragment)
				EmergencyRoute.GoSettings -> navController.navigate(R.id.settingsFragment)
				EmergencyRoute.GoPinCode -> navController.navigate(
					R.id.pinCodeFragment,
					PinCodeFragment.createArgs(PinCodeFlow.SOS_PIN_CODE)
				)
				EmergencyRoute.CheckPermission -> checkPermissions()
				is EmergencyRoute.ControlFlash -> controlFlashLight(route.enabled)
			}
		}
		return ComposeView(requireContext()).apply {
			setViewCompositionStrategy(
				ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
					viewLifecycleOwner
				)
			)
			
			setContent {
				EmergencyScreenContent(viewModel)
			}
		}
	}
	
	private fun controlFlashLight(enabled: Boolean) {
		if (activity?.packageManager?.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) == true) {
			if (activity?.packageManager?.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH) == true) {
				val cameraID = cameraManager?.cameraIdList?.get(0)
				if (cameraID != null) {
					cameraManager?.setTorchMode(cameraID, enabled)
				}
			}
		}
	}
	
	private fun checkPermissions() {
		if (permissionsDelegate.checkSelfPermissions()) {
		
		} else {
			pendingAction = {
			
			}
			permissionsDelegate.requestPermissions()
		}
	}
	
	private fun onPermissionsGranted(isGranted: Boolean) {
		if (isGranted) {
			pendingAction?.invoke()
			pendingAction = null
		}
	}
	
	override fun onResume() {
		super.onResume()
		hideSystemUI()
	}
	
	override fun onStop() {
		super.onStop()
		showSystemUI()
	}
}