package com.civilcam.ui.emergency.content

import android.net.Uri
import android.util.Size
import androidx.camera.core.CameraInfo
import androidx.camera.core.TorchState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import com.civilcam.ui.emergency.EmergencyViewModel
import com.civilcam.ui.emergency.model.CameraEffect
import com.civilcam.ui.emergency.model.EmergencyActions
import com.civilcam.ui.emergency.utils.LocalVideoCaptureManager
import com.civilcam.ui.emergency.utils.PreviewState
import com.civilcam.ui.emergency.utils.VideoCaptureManager

@Composable
internal fun EmergencyLivePreview(
	recordingViewModel: EmergencyViewModel,
	onActionClick: (EmergencyActions) -> Unit
) {
	val state by recordingViewModel.state.collectAsState()
	val context = LocalContext.current
	val lifecycleOwner = LocalLifecycleOwner.current
	
	val listener = remember {
		object : VideoCaptureManager.Listener {
			override fun onInitialised(cameraLensInfo: HashMap<Int, CameraInfo>) {
				onActionClick.invoke(EmergencyActions.CameraInitialized(cameraLensInfo))
			}
			
			override fun recordingPaused() {}
			
			override fun onProgress(progress: Int) {}
			
			override fun recordingCompleted(outputUri: Uri) {}
			
			override fun onError(throwable: Throwable?) {}
		}
	}
	
	val captureManager = remember(recordingViewModel) {
		VideoCaptureManager.Builder(context)
			.registerLifecycleOwner(lifecycleOwner)
			.create()
			.apply { this.listener = listener }
	}
	
	CompositionLocalProvider(LocalVideoCaptureManager provides captureManager) {
		VideoScreenContent(
			cameraLens = state.lens,
			torchState = state.torchState
		)
	}
	
	LaunchedEffect(recordingViewModel) {
		recordingViewModel.effect.collect {
			when (it) {
				is CameraEffect.RecordVideo -> captureManager.startRecording(it.filePath)
				CameraEffect.StopRecording -> captureManager.stopRecording()
			}
		}
	}
}

@Composable
private fun VideoScreenContent(
	cameraLens: Int?,
	@TorchState.State torchState: Int
) {
	Box(modifier = Modifier.fillMaxSize()) {
		cameraLens?.let {
			CameraPreview(lens = it, torchState = torchState)
		}
	}
}

@Composable
private fun CameraPreview(lens: Int, @TorchState.State torchState: Int) {
	val captureManager = LocalVideoCaptureManager.current
	BoxWithConstraints {
		AndroidView(
			factory = {
				captureManager.showPreview(
					PreviewState(
						cameraLens = lens,
						torchState = torchState,
						size = Size(this.minWidth.value.toInt(), this.maxHeight.value.toInt())
					)
				)
			},
			modifier = Modifier.fillMaxSize(),
			update = {
				captureManager.updatePreview(
					PreviewState(cameraLens = lens, torchState = torchState),
					it
				)
			}
		)
	}
}