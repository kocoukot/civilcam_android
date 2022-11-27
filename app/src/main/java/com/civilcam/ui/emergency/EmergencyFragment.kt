package com.civilcam.ui.emergency

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.civilcam.BuildConfig
import com.civilcam.R
import com.civilcam.databinding.FragmentLiveBinding
import com.civilcam.domainLayer.EmergencyScreen
import com.civilcam.ext_features.alert.AlertDialogButtons
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
import com.pedro.rtplibrary.rtmp.RtmpCamera1
import net.ossrs.rtmp.ConnectCheckerRtmp
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class EmergencyFragment : Fragment(R.layout.fragment_live), ConnectCheckerRtmp,
	SurfaceHolder.Callback {
	private val binding by viewBinding(FragmentLiveBinding::bind)
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
	}
	
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_live, container, false)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		rtmpCamera = RtmpCamera1(binding.liveSurfaceView.surfaceView, this)
		rtmpCamera?.setReTries(1000)
		binding.liveSurfaceView.surfaceView.apply {
			holder.addCallback(this@EmergencyFragment)
		}
		
		with(binding) {
			composable.setContent {
				LiveScreenContent(viewModel = viewModel)
			}
			liveSurfaceView.videoScale.setOnClickListener {
				viewModel.setInputActions(EmergencyActions.ChangeLiveScreen)
			}
			liveSurfaceView.cameraTorch.setOnClickListener {
				viewModel.setInputActions(EmergencyActions.ControlTorch)
			}
			liveSurfaceView.cameraSwitch.setOnClickListener {
				switchCamera()
			}
			liveSurfaceView.backButton.setOnClickListener {
				viewModel.setInputActions(EmergencyActions.GoBack)
			}
		}
		
		setFragmentResultListener(PinCodeFragment.RESULT_BACK_STACK) { _, _ ->
			viewModel.setInputActions(EmergencyActions.DisableSos)
		}
		
		setFragmentResultListener(PinCodeFragment.RESULT_SAVED_NEW_PIN) { _, _ ->
		
		}
		
		viewModel.steps.observeNonNull(viewLifecycleOwner) { route ->
			when (route) {
				EmergencyRoute.GoUserProfile -> navController.navigate(R.id.userProfileFragment)
				EmergencyRoute.GoSettings -> navController.navigate(R.id.settingsFragment)
				EmergencyRoute.GoPinCode -> navController.navigate(
					R.id.pinCodeFragment,
					PinCodeFragment.createArgs(PinCodeFlow.SOS_PIN_CODE, false)
				)
				EmergencyRoute.HideSystemUI -> hideSystemUI()
				EmergencyRoute.ShowSystemUI -> showSystemUI()
				is EmergencyRoute.CheckPermission -> checkPermissions(route.isSos)
				is EmergencyRoute.IsNavBarVisible -> (activity as MainActivity).showBottomNavBar(
					route.isVisible
				)
				is EmergencyRoute.GoLive -> goLive(route.streamKey)
				else -> {}
			}
		}
		
		observeLiveData()
	}
	
	private fun observeLiveData() = with(viewModel) {
		screenState.observeNonNull(viewLifecycleOwner) {
			changeScreenState(it)
		}
		currentTime.observeNonNull(viewLifecycleOwner) {
			binding.liveSurfaceView.videoCurrentTime.text = it
		}
		stopStream.observeNonNull(viewLifecycleOwner) {
			stopStream()
		}
		controlTorch.observeNonNull(viewLifecycleOwner) {
			controlTorch(it)
		}
	}
	
	@SuppressLint("UseCompatLoadingForDrawables")
	private fun changeScreenState(screen: EmergencyScreen) {
		Timber.i("Live screen state: $screen")
		with(binding) {
			when (screen) {
				EmergencyScreen.NORMAL -> {
					livePreview.isVisible = false
					mapPreview.isVisible = true
					
					liveSurfaceView.liveExtendedBlock.isVisible = false
				}
				EmergencyScreen.COUPLED -> {
					livePreview.isVisible = true
					mapPreview.isVisible = true
					
					liveSurfaceView.liveExtendedBlock.isVisible = false
					liveSurfaceView.liveToolbar.isVisible = false
					liveSurfaceView.videoScale.setImageDrawable(
						resources.getDrawable(
							R.drawable.ic_live_extend, null
						)
					)
				}
				EmergencyScreen.LIVE_EXTENDED -> {
					livePreview.isVisible = true
					mapPreview.isVisible = false
					
					liveSurfaceView.liveExtendedBlock.isVisible = true
					liveSurfaceView.liveToolbar.isVisible = true
					liveSurfaceView.videoScale.setImageDrawable(
						resources.getDrawable(
							R.drawable.ic_live_minimize, null
						)
					)
				}
				EmergencyScreen.MAP_EXTENDED -> {
					livePreview.isVisible = false
					mapPreview.isVisible = true
				}
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
				alertType = AlertDialogButtons.OK,
				onOptionSelected = {})
		}
	}
	
	private fun switchCamera() {
		rtmpCamera?.switchCamera()
	}
	
	private fun stopStream() {
		rtmpCamera?.stopStream()
	}
	
	private fun goLive(streamKey: String) {
		rtmpCamera?.apply {
			prepareVideo()
			prepareAudio(
				128 * 1024, 48000, true
			)
			startStream(BuildConfig.RTMP_ENDPOINT + streamKey)
		}
	}
	
	@SuppressLint("UseCompatLoadingForDrawables")
	private fun controlTorch(isEnable: Boolean) {
		if (isEnable) {
			binding.liveSurfaceView.cameraTorch.setImageDrawable(
				resources.getDrawable(
					R.drawable.ic_flash_on, null
				)
			)
		} else {
			binding.liveSurfaceView.cameraTorch.setImageDrawable(
				resources.getDrawable(
					R.drawable.ic_flash_off, null
				)
			)
		}
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
//		if (rtmpCamera?.isRecording == true) {
//			rtmpCamera?.pauseRecord()
//		}
	}
	
	override fun onResume() {
		super.onResume()
		viewModel.loadAvatar()
		viewModel.addListeners()
//		if (rtmpCamera?.isStreaming == true) {
//			rtmpCamera?.resumeRecord()
//		}
	}
	
	override fun onConnectionSuccessRtmp() {}
	
	override fun onConnectionFailedRtmp(reason: String) {
		rtmpCamera?.reTry(5000, reason)
	}
	
	override fun onNewBitrateRtmp(bitrate: Long) {}
	
	override fun onDisconnectRtmp() {}
	
	override fun onAuthErrorRtmp() {}
	
	override fun onAuthSuccessRtmp() {}
	
	override fun surfaceCreated(p0: SurfaceHolder) {}
	
	override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {}
	
	override fun surfaceDestroyed(p0: SurfaceHolder) {}
	
}