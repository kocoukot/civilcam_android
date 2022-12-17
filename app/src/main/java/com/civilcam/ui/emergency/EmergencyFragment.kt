package com.civilcam.ui.emergency

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.CAMERA_SERVICE
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.civilcam.BuildConfig
import com.civilcam.R
import com.civilcam.databinding.FragmentLiveBinding
import com.civilcam.domainLayer.EmergencyScreen
import com.civilcam.ext_features.alert.AlertDialogButtons
import com.civilcam.ext_features.arch.VoiceRecord
import com.civilcam.ext_features.arg
import com.civilcam.ext_features.ext.hideSystemUI
import com.civilcam.ext_features.ext.showSystemUI
import com.civilcam.ext_features.ext.showToast
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.ossrs.rtmp.ConnectCheckerRtmp
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber


class EmergencyFragment : Fragment(R.layout.fragment_live), ConnectCheckerRtmp,
	SurfaceHolder.Callback {
	private val binding by viewBinding(FragmentLiveBinding::bind)
	private val viewModel: EmergencyViewModel by viewModel { parametersOf(isVoiceActivation) }

	private val isVoiceActivation by arg(ARG_IS_VOICE_ACTIVATION, false)
	private val permissionsDelegate = registerForPermissionsResult(
		Manifest.permission.ACCESS_FINE_LOCATION,
		Manifest.permission.ACCESS_COARSE_LOCATION,
		Manifest.permission.CAMERA,
		Manifest.permission.RECORD_AUDIO
	) { onPermissionsGranted(it) }

	private var pendingAction: (() -> Unit)? = null
	private val mSocket = SocketHandler
	private var rtmpCamera: RtmpCamera1? = null
	private var cameraManager: CameraManager? = null
	
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
		activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
		
		cameraManager = activity?.getSystemService(CAMERA_SERVICE) as CameraManager
		
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

		lifecycleScope.launch {
			repeatOnLifecycle(Lifecycle.State.RESUMED) {
				streamKey.onEach { streamKey ->
					delay(500)
					goLive(streamKey)
				}.launchIn(lifecycleScope)
			}
		}
	}
	
	@SuppressLint("UseCompatLoadingForDrawables")
	private fun changeScreenState(screen: EmergencyScreen) {
		Timber.tag("stream_flow").i("Live screen state: $screen")
		with(binding) {
			if (screen == EmergencyScreen.NORMAL)
				(requireActivity() as VoiceRecord).startVoiceRecord()
			else
				(requireActivity() as VoiceRecord).stopVoiceRecord()

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
		rtmpCamera = RtmpCamera1(binding.liveSurfaceView.surfaceView, this)
		rtmpCamera?.setReTries(1000)
		binding.liveSurfaceView.surfaceView.apply {
			holder.addCallback(this@EmergencyFragment)
		}
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
		binding.liveSurfaceView.cameraTorch.isActivated = isEnable
		if (activity?.packageManager?.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) == true) {
			if (activity?.packageManager?.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH) == true) {
				try {
					cameraManager?.setTorchMode("0", isEnable)
				} catch (e: CameraAccessException) {
					showToast("Torch error ${e.localizedMessage}")
				}
			} else {
				showToast("This device has no flash")
			}
		} else {
			showToast("This device has no camera")
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
		try {
			rtmpCamera?.reTry(5000, reason)
		} catch (e: Throwable) {
			Timber.e("rtmpCamera error ${e.localizedMessage}")
		}
	}
	
	override fun onNewBitrateRtmp(bitrate: Long) {}
	
	override fun onDisconnectRtmp() {}

	override fun onAuthErrorRtmp() {}

	override fun onAuthSuccessRtmp() {}

	override fun surfaceCreated(p0: SurfaceHolder) {}

	override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {}

	override fun surfaceDestroyed(p0: SurfaceHolder) {}

	companion object {
		private const val ARG_IS_VOICE_ACTIVATION = "is_voice"

		fun createArgs(isVoice: Boolean) = bundleOf(
			ARG_IS_VOICE_ACTIVATION to isVoice
		)
	}

}