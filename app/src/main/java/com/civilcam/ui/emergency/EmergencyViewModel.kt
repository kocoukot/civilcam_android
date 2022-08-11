package com.civilcam.ui.emergency

import android.net.Uri
import androidx.camera.core.CameraInfo
import androidx.camera.core.CameraSelector
import androidx.camera.core.TorchState
import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.ui.emergency.model.*
import com.civilcam.ui.emergency.utils.FileManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class EmergencyViewModel: ComposeViewModel<EmergencyState, EmergencyRoute, EmergencyActions>() {
	override var _state: MutableStateFlow<EmergencyState> = MutableStateFlow(EmergencyState())
	
	private val _effect = MutableSharedFlow<CameraEffect>()
	val effect: SharedFlow<CameraEffect> = _effect
	
	override fun setInputActions(action: EmergencyActions) {
		when (action) {
			EmergencyActions.DoubleClickSos -> doubleClickSos()
			EmergencyActions.DisableSos -> disableSosStatus()
			EmergencyActions.GoUserProfile -> goUserProfile()
			EmergencyActions.GoSettings -> goSettings()
			EmergencyActions.OneClickSafe -> oneClickSafe()
			EmergencyActions.GoBack -> goBack()
			EmergencyActions.ControlFlash -> onFlashTapped()
			EmergencyActions.ChangeCamera -> onCameraFlip()
			is EmergencyActions.ChangeMode -> changeMode(action.mode)
			is EmergencyActions.ClickChangeScreen -> screenChange(action.screenState)
			is EmergencyActions.CameraInitialized -> onCameraInitialized(action.cameraLensInfo)
//            EmergencyActions.DetectLocation -> TODO()
			else -> {}
		}
	}
	
	private fun screenChange(newScreenState: EmergencyScreen) {
		_state.update { it.copy(emergencyScreen = newScreenState) }
	}
	
	private fun changeMode(screen: EmergencyScreen) {
		Timber.d("changeMode $screen")
		_state.value = _state.value.copy(emergencyScreen = screen)
		steps.value = EmergencyRoute.IsNavBarVisible(screen == EmergencyScreen.NORMAL)
		when (screen) {
			EmergencyScreen.NORMAL,
			EmergencyScreen.COUPLED -> {
				onRecordStarted()
				navigateRoute(EmergencyRoute.HideSystemUI)
			}
			EmergencyScreen.MAP_EXTENDED, EmergencyScreen.LIVE_EXTENDED -> navigateRoute(
				EmergencyRoute.ShowSystemUI
			)
		}
	}
	
	private fun goBack() {
		navigateRoute(EmergencyRoute.HideSystemUI)
		_state.value = _state.value.copy(emergencyScreen = EmergencyScreen.COUPLED)
	}
	
	private fun goSettings() {
		navigateRoute(EmergencyRoute.GoSettings)
	}
	
	private fun goUserProfile() {
		navigateRoute(EmergencyRoute.GoUserProfile)
	}
	
	private fun goPinCode() {
		navigateRoute(EmergencyRoute.GoPinCode)
	}
	
	private fun doubleClickSos() {
		navigateRoute(EmergencyRoute.CheckPermission)
	}
	
	private fun oneClickSafe() {
		if (state.value.emergencyButton == EmergencyButton.InDangerButton) {
			goPinCode()
		}
	}
	
	fun launchSos() {
		steps.value = EmergencyRoute.IsNavBarVisible(false)
		if (state.value.emergencyButton == EmergencyButton.InSafeButton) {
			_state.value = _state.value.copy(
				emergencyScreen = EmergencyScreen.COUPLED,
				emergencyButton = EmergencyButton.InDangerButton
			)
		}
	}
	
	private fun disableSosStatus() {
		_state.value = _state.value.copy(
			emergencyScreen = EmergencyScreen.NORMAL,
			emergencyButton = EmergencyButton.InSafeButton
		)
	}
	
	fun isLocationAllowed(isAllowed: Boolean) {
		_state.update { it.copy(isLocationAllowed = isAllowed) }
	}
	
	private fun onStopTapped() {
		viewModelScope.launch {
			_effect.emit(CameraEffect.StopRecording)
		}
	}

	private fun onRecordStarted() {
		viewModelScope.launch {
			try {
				//val filePath = fileManager.createFile("videos", "mp4")
				_effect.emit(CameraEffect.RecordVideo("filePath"))
			} catch (exception: IllegalArgumentException) {
				Timber.e(exception)
			}
		}
	}
	
	private fun onCameraInitialized(cameraLensInfo: HashMap<Int, CameraInfo>) {
		if (cameraLensInfo.isNotEmpty()) {
			val defaultLens = if (cameraLensInfo[CameraSelector.LENS_FACING_BACK] != null) {
				CameraSelector.LENS_FACING_BACK
			} else if (cameraLensInfo[CameraSelector.LENS_FACING_BACK] != null) {
				CameraSelector.LENS_FACING_FRONT
			} else {
				null
			}
			_state.update {
				it.copy(
					lens = it.lens ?: defaultLens,
					lensInfo = cameraLensInfo
				)
			}
		}
	}
	
	private fun onCameraFlip() {
		val lens = if (_state.value.lens == CameraSelector.LENS_FACING_FRONT) {
			CameraSelector.LENS_FACING_BACK
		} else {
			CameraSelector.LENS_FACING_FRONT
		}
		_state.update { it.copy(lens = lens) }
	}
	
	private fun onFlashTapped() {
		_state.update {
			when (_state.value.torchState) {
				TorchState.OFF -> it.copy(torchState = TorchState.ON)
				TorchState.ON -> it.copy(torchState = TorchState.OFF)
				else -> it.copy(torchState = TorchState.OFF)
			}
		}
	}
}


    



