package com.civilcam.ui.emergency

import androidx.camera.core.CameraSelector
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.ui.emergency.model.*
import kotlinx.coroutines.flow.MutableStateFlow


class EmergencyViewModel : ComposeViewModel<EmergencyState, EmergencyRoute, EmergencyActions>() {
	override var _state: MutableStateFlow<EmergencyState> = MutableStateFlow(EmergencyState())
	
	init {
		updateNavBar()
	}
	
	override fun setInputActions(action: EmergencyActions) {
		when (action) {
			EmergencyActions.DoubleClickSos -> doubleClickSos()
			EmergencyActions.GoUserProfile -> goUserProfile()
			EmergencyActions.GoSettings -> goSettings()
			EmergencyActions.OneClickSafe -> oneClickSafe()
			EmergencyActions.GoBack -> goBack()
			EmergencyActions.ControlFlash -> controlFlash()
			EmergencyActions.ChangeCamera -> changeCamera()
			is EmergencyActions.ChangeMode -> changeMode(action.mode)
		}
	}
	
	private fun changeCamera() {
		if (_state.value.cameraState == CameraSelector.LENS_FACING_BACK) {
			_state.value = _state.value.copy(cameraState = CameraSelector.LENS_FACING_FRONT)
		} else {
			_state.value = _state.value.copy(cameraState = CameraSelector.LENS_FACING_BACK)
		}
	}
	
	private fun updateNavBar(){
		_steps.value = EmergencyRoute.ScreenState(_state.value.emergencyScreen)
	}
	
	private fun controlFlash() {
		_steps.value = EmergencyRoute.ControlFlash(_state.value.cameraFlash, _state.value.cameraState)
		_state.value = _state.value.copy(cameraFlash = !_state.value.cameraFlash)
	}
	
	private fun changeMode(screen: EmergencyScreen) {
		_state.value = _state.value.copy(emergencyScreen = screen)
		updateNavBar()
	}
	
	private fun goBack() {
		_state.value = _state.value.copy(emergencyScreen = EmergencyScreen.COUPLED)
		updateNavBar()
	}
	
	private fun goSettings() {
		_steps.value = EmergencyRoute.GoSettings
	}
	
	private fun goUserProfile() {
		_steps.value = EmergencyRoute.GoUserProfile
	}
	
	private fun goPinCode() {
		_steps.value = EmergencyRoute.GoPinCode
	}
	
	private fun oneClickSafe() {
		if (state.value.emergencyButton == EmergencyButton.InDangerButton) {
			goPinCode()
		}
	}
	
	private fun doubleClickSos() {
		_steps.value = EmergencyRoute.CheckPermission
		if (state.value.emergencyButton == EmergencyButton.InSafeButton) {
			_state.value = _state.value.copy(emergencyScreen = EmergencyScreen.COUPLED)
			_state.value = _state.value.copy(emergencyButton = EmergencyButton.InDangerButton)
		}
		updateNavBar()
	}
	
	fun disableSosStatus() {
		_state.value = _state.value.copy(emergencyScreen = EmergencyScreen.NORMAL)
		_state.value = _state.value.copy(emergencyButton = EmergencyButton.InSafeButton)
		updateNavBar()
	}
}


    



