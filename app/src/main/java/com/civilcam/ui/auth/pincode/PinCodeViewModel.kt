package com.civilcam.ui.auth.pincode

import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.ui.auth.pincode.model.*
import kotlinx.coroutines.flow.MutableStateFlow

class PinCodeViewModel :
	ComposeViewModel<PinCodeState, PinCodeRoute, PinCodeActions>() {
	override var _state: MutableStateFlow<PinCodeState> = MutableStateFlow(PinCodeState())
	
	override fun setInputActions(action: PinCodeActions) {
		when (action) {
			PinCodeActions.GoBack -> goBack()
			PinCodeActions.GoBackConfirm -> backStackScreenState()
			is PinCodeActions.EnterPinCode -> {
				when (action.inputType) {
					PinCodeInputDataType.PIN -> pinEntered(action.pinCode)
					PinCodeInputDataType.PIN_CONFIRM -> pinConfirmEntered(action.pinCode)
				}
			}
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
	
	private fun goBack() {
		_steps.value = PinCodeRoute.GoBack
	}
	
	private fun goGuardians() {
		_steps.value = PinCodeRoute.GoGuardians
	}
	
	private fun goConfirm() {
		_state.value = _state.value.copy(screenState = PinCodeScreen.PIN_CODE_CONFIRM)
	}
	
	fun backStackScreenState() {
		_state.value = _state.value.copy(screenState = PinCodeScreen.PIN_CODE)
	}
	
	fun clearStates() {
		_state.value = _state.value.copy(confirmPinCode = "")
	}
}