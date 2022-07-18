package com.civilcam.ui.auth.pincode

import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.ui.auth.pincode.model.PinCodeActions
import com.civilcam.ui.auth.pincode.model.PinCodeFlow
import com.civilcam.ui.auth.pincode.model.PinCodeRoute
import com.civilcam.ui.auth.pincode.model.PinCodeState
import kotlinx.coroutines.flow.MutableStateFlow

class PinCodeViewModel(
	pinCodeFlow: PinCodeFlow
) :
	ComposeViewModel<PinCodeState, PinCodeRoute, PinCodeActions>() {
	override var _state: MutableStateFlow<PinCodeState> = MutableStateFlow(PinCodeState())
	
	init {
		_state.value = _state.value.copy(screenState = pinCodeFlow)
	}
	
	override fun setInputActions(action: PinCodeActions) {
		when (action) {
			PinCodeActions.GoBack -> goBack()
			is PinCodeActions.EnterPinCode -> {
				when (action.pinCodeFlow) {
					PinCodeFlow.CREATE_PIN_CODE -> pinEntered(action.pinCode)
					PinCodeFlow.CONFIRM_PIN_CODE -> pinConfirmEntered(action.pinCode)
					PinCodeFlow.CURRENT_PIN_CODE -> currentPinEntered(action.pinCode)
					PinCodeFlow.NEW_PIN_CODE -> newPinCodeEntered(action.pinCode)
					PinCodeFlow.CONFIRM_NEW_PIN_CODE -> newPinCodeConfirmEntered(action.pinCode)
					PinCodeFlow.SOS_PIN_CODE -> sosPinCodeEntered(action.pinCode)
				}
			}
		}
	}
	
	private fun sosPinCodeEntered(pinCode: String) {
		_state.value = _state.value.copy(sosPinCode = pinCode)
		if (_state.value.sosMatch) {
			_state.value = _state.value.copy(currentNoMatch = false)
			goEmergency()
		} else {
			_state.value = _state.value.copy(currentNoMatch = true)
		}
	}
	
	private fun currentPinEntered(pinCode: String) {
		_state.value = _state.value.copy(currentPinCode = pinCode)
		if (_state.value.isCurrentPin) {
			_state.value = _state.value.copy(currentNoMatch = false)
			goNewPinCode()
		} else {
			_state.value = _state.value.copy(currentNoMatch = true)
		}
	}
	
	private fun newPinCodeEntered(pinCode: String) {
		_state.value = _state.value.copy(newPinCode = pinCode)
		goNewPinCodeConfirm()
	}
	
	private fun newPinCodeConfirmEntered(pinCode: String) {
		_state.value = _state.value.copy(newPinCodeConfirm = pinCode)
		if (_state.value.isMatchNewPin) {
			_state.value = _state.value.copy(newPinNoMatch = false)
			goUserProfile()
		} else {
			_state.value = _state.value.copy(newPinNoMatch = true)
		}
	}
	
	private fun pinConfirmEntered(pinCode: String) {
		_state.value = _state.value.copy(confirmPinCode = pinCode)
		if (_state.value.isMatch) {
			goGuardians()
		} else {
			_state.value = _state.value.copy(noMatch = true)
		}
	}
	
	private fun pinEntered(pinCode: String) {
		_state.value = _state.value.copy(pinCode = pinCode)
		goConfirm()
	}
	
	private fun goUserProfile() {
		_steps.value = PinCodeRoute.GoUserProfile
	}
	
	private fun goBack() {
		_state.value = _state.value.copy(currentNoMatch = false, newPinNoMatch = false)
		when (_state.value.screenState) {
			PinCodeFlow.SOS_PIN_CODE -> _steps.value = PinCodeRoute.GoBack
			PinCodeFlow.CREATE_PIN_CODE -> _steps.value = PinCodeRoute.GoBack
			PinCodeFlow.CONFIRM_PIN_CODE -> _state.value =
				_state.value.copy(screenState = PinCodeFlow.CREATE_PIN_CODE, confirmPinCode = "")
			PinCodeFlow.CURRENT_PIN_CODE -> goUserProfile()
			PinCodeFlow.NEW_PIN_CODE -> _state.value =
				_state.value.copy(screenState = PinCodeFlow.CURRENT_PIN_CODE)
			PinCodeFlow.CONFIRM_NEW_PIN_CODE -> _state.value =
				_state.value.copy(screenState = PinCodeFlow.NEW_PIN_CODE, confirmPinCode = "")
		}
	}
	
	private fun goEmergency() {
		_state.value = _state.value.copy(currentNoMatch = false)
		_steps.value = PinCodeRoute.GoEmergency
	}
	
	private fun goGuardians() {
		_steps.value = PinCodeRoute.GoGuardians
	}
	
	private fun goNewPinCodeConfirm() {
		_state.value = _state.value.copy(screenState = PinCodeFlow.CONFIRM_NEW_PIN_CODE)
	}
	
	private fun goNewPinCode() {
		_state.value = _state.value.copy(screenState = PinCodeFlow.NEW_PIN_CODE)
	}
	
	private fun goConfirm() {
		_state.value = _state.value.copy(screenState = PinCodeFlow.CONFIRM_PIN_CODE)
	}
	
	fun clearStates() {
		_state.value = _state.value.copy(confirmPinCode = "", noMatch = false)
	}
}