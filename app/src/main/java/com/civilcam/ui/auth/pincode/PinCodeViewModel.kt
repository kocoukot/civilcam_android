package com.civilcam.ui.auth.pincode

import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.ui.auth.pincode.model.PinCodeActions
import com.civilcam.ui.auth.pincode.model.PinCodeInputDataType
import com.civilcam.ui.auth.pincode.model.PinCodeRoute
import com.civilcam.ui.auth.pincode.model.PinCodeState
import kotlinx.coroutines.flow.MutableStateFlow

class PinCodeViewModel(isConfirm: Boolean) :
	ComposeViewModel<PinCodeState, PinCodeRoute, PinCodeActions>() {
	override var _state: MutableStateFlow<PinCodeState> = MutableStateFlow(PinCodeState())
	
	init {
		_state.value = _state.value.copy(isConfirm = isConfirm)
	}
	
	override fun setInputActions(action: PinCodeActions) {
		when (action) {
			PinCodeActions.GoBack -> goBack()
			PinCodeActions.GoConfirm -> goConfirm()
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
		}
	}
	
	private fun pinEntered(pinCode: String) {
		_state.value = _state.value.copy(pinCode = pinCode)
	}
	
	private fun goBack() {
		_steps.value = PinCodeRoute.GoBack
	}
	
	private fun goGuardians() {
		_steps.value = PinCodeRoute.GoGuardians
	}
	
	private fun goConfirm() {
		_steps.value = PinCodeRoute.GoConfirm
	}
}