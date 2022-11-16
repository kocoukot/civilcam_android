package com.civilcam.ui.emergency

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.civilcam.BuildConfig
import com.civilcam.R
import com.civilcam.databinding.FragmentEmergencyBinding
import com.civilcam.ext_features.alert.AlertDialogTypes
import com.civilcam.ext_features.ext.hideSystemUI
import com.civilcam.ext_features.ext.showSystemUI
import com.civilcam.ext_features.live_data.observeNonNull
import com.civilcam.ext_features.navController
import com.civilcam.ext_features.registerForPermissionsResult
import com.civilcam.ext_features.viewBinding
import com.civilcam.socket_feature.SocketHandler
import com.civilcam.ui.MainActivity
import com.civilcam.ui.auth.pincode.PinCodeFragment
import com.civilcam.ui.auth.pincode.model.PinCodeFlow
import com.civilcam.ui.common.alert.DialogAlertFragment
import com.civilcam.ui.emergency.model.EmergencyActions
import com.civilcam.ui.emergency.model.EmergencyRoute
import com.pedro.encoder.input.video.CameraHelper
import com.pedro.rtplibrary.rtmp.RtmpCamera1
import net.ossrs.rtmp.ConnectCheckerRtmp
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class EmergencyFragment : Fragment(R.layout.fragment_emergency), ConnectCheckerRtmp {
	private val viewModel: EmergencyViewModel by viewModel()
	
	private val permissionsDelegate = registerForPermissionsResult(
		Manifest.permission.ACCESS_FINE_LOCATION,
		Manifest.permission.ACCESS_COARSE_LOCATION,
		Manifest.permission.CAMERA,
		Manifest.permission.RECORD_AUDIO
	) { onPermissionsGranted(it) }
	
	private var pendingAction: (() -> Unit)? = null
	
	private val mSocket = SocketHandler
	private var rtmpCamera: RtmpCamera1? = null
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		mSocket.setSocket()
		mSocket.establishConnection()
		rtmpCamera = RtmpCamera1(requireContext(), this)
		rtmpCamera?.setReTries(1000)
	}
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		setFragmentResultListener(PinCodeFragment.RESULT_BACK_STACK) { _, _ ->
			viewModel.setInputActions(EmergencyActions.DisableSos)
		}
		
		viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
			when (route) {
				EmergencyRoute.GoUserProfile -> navController.navigate(R.id.userProfileFragment)
				EmergencyRoute.GoSettings -> navController.navigate(R.id.settingsFragment)
				EmergencyRoute.GoPinCode -> navController.navigate(
					R.id.pinCodeFragment,
					PinCodeFragment.createArgs(PinCodeFlow.SOS_PIN_CODE, false)
				)
				EmergencyRoute.HideSystemUI -> {
					rtmpCamera?.stopStream()
					hideSystemUI()
				}
				EmergencyRoute.ShowSystemUI -> showSystemUI()
				EmergencyRoute.StopStream -> stopStream()
				EmergencyRoute.ChangeCamera -> rtmpCamera?.switchCamera()
				is EmergencyRoute.CheckPermission -> checkPermissions(route.isSos)
				is EmergencyRoute.IsNavBarVisible -> (activity as MainActivity).showBottomNavBar(
					route.isVisible
				)
				is EmergencyRoute.GoLive -> goLive(route.streamKey)
				
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
	
	private fun checkPermissions(launchSos: Boolean = false) {
		if (permissionsDelegate.checkSelfPermissions()) {
			if (launchSos) viewModel.launchSos() else {
				viewModel.fetchUserLocation()
				(activity as MainActivity).startLocationService()
			}
		} else {
			pendingAction = { checkPermissions() }
			permissionsDelegate.requestPermissions()
		}
		viewModel.isLocationAllowed(permissionsDelegate.checkSelfPermissions())
	}
	
	private fun onPermissionsGranted(isGranted: Boolean) {
		Timber.i("onPermissionsGranted $isGranted")
		if (isGranted) {
			pendingAction?.invoke()
			pendingAction = null
		} else {
			DialogAlertFragment.create(fragmentManager = parentFragmentManager,
				title = getString(R.string.location_alert_title),
				text = getString(R.string.location_alert_text),
				alertType = AlertDialogTypes.OK,
				onOptionSelected = {})
		}
	}
	
	private fun stopStream() {
		rtmpCamera?.stopStream()
	}
	
	private fun goLive(streamKey: String) {
		rtmpCamera?.prepareVideo(
			640, 360, 30, 1000 * 1024, 2, CameraHelper.getCameraOrientation(requireContext())
		)
		rtmpCamera?.prepareAudio(
			128 * 1024, 48000, true
		)
		rtmpCamera?.startStream(BuildConfig.RTMP_ENDPOINT + streamKey)
	}
	
	override fun onStart() {
		super.onStart()
		checkPermissions()
		viewModel.screenStateCheck()
	}
	
	override fun onStop() {
		super.onStop()
		showSystemUI()
		viewModel.removeSocketListeners()
	}
	
	override fun onResume() {
		super.onResume()
		viewModel.loadAvatar()
		viewModel.addListeners()
	}
	
	override fun onConnectionSuccessRtmp() {}
	
	override fun onConnectionFailedRtmp(reason: String) {}
	
	override fun onNewBitrateRtmp(bitrate: Long) {}
	
	override fun onDisconnectRtmp() {}
	
	override fun onAuthErrorRtmp() {}
	
	override fun onAuthSuccessRtmp() {}
}