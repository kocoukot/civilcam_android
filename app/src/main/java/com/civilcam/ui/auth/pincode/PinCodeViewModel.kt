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
			is PinCodeActions.EnterPinCode -> pinEntered(action.pinCode)
		}
	}
	
	private fun pinEntered(pinCode: String) {
		when (_state.value.screenState) {
			PinCodeFlow.CREATE_PIN_CODE -> {
				_state.value = _state.value.copy(pinCode = pinCode)
				if (_state.value.pinCode.length == PIN_SIZE) {
					goConfirm()
				}
			}
			PinCodeFlow.CONFIRM_PIN_CODE -> {
				_state.value = _state.value.copy(confirmPinCode = pinCode)
				if (_state.value.isMatch) {
					_state.value = _state.value.copy(noMatch = false)
					goGuardians()
				} else {
					if (_state.value.confirmPinCode.length == PIN_SIZE) {
						_state.value = _state.value.copy(confirmPinCode = "")
						_state.value = _state.value.copy(noMatch = true)
					}
				}
			}
			PinCodeFlow.NEW_PIN_CODE -> {
				_state.value = _state.value.copy(pinCode = pinCode)
				if (_state.value.pinCode.length == PIN_SIZE) {
					goNewPinCodeConfirm()
				}
			}
			PinCodeFlow.CONFIRM_NEW_PIN_CODE -> {
				_state.value = _state.value.copy(confirmPinCode = pinCode)
				if (_state.value.isMatch) {
					_state.value = _state.value.copy(newPinNoMatch = false)
					goUserProfile()
				} else {
					if (_state.value.confirmPinCode.length == PIN_SIZE) {
						_state.value = _state.value.copy(confirmPinCode = "")
						_state.value = _state.value.copy(newPinNoMatch = true)
					}
				}
			}
			PinCodeFlow.CURRENT_PIN_CODE -> {
				_state.value = _state.value.copy(pinCode = pinCode)
				if (_state.value.isCurrentPin) {
					_state.value = _state.value.copy(currentNoMatch = false)
					_state.value = _state.value.copy(pinCode = "")
					goNewPinCode()
				} else {
					if (_state.value.pinCode.length == PIN_SIZE) {
						_state.value = _state.value.copy(pinCode = "")
					}
					_state.value = _state.value.copy(currentNoMatch = true)
				}
			}
			PinCodeFlow.SOS_PIN_CODE -> {
				_state.value = _state.value.copy(pinCode = pinCode)
				if (_state.value.sosMatch) {
					_state.value = _state.value.copy(currentNoMatch = false)
					if (_state.value.pinCode.length == PIN_SIZE) {
						goEmergency()
					}
				} else {
					if (_state.value.pinCode.length == PIN_SIZE) {
						_state.value = _state.value.copy(pinCode = "")
					}
					_state.value = _state.value.copy(currentNoMatch = true)
				}
			}
		}
	}
	
	private fun goUserProfile() {
		navigateRoute(PinCodeRoute.GoUserProfile)
	}
	
	private fun goBack() {
		_state.value = _state.value.copy(currentNoMatch = false, newPinNoMatch = false)
		when (_state.value.screenState) {
			PinCodeFlow.SOS_PIN_CODE -> navigateRoute(PinCodeRoute.GoBack)
			PinCodeFlow.CREATE_PIN_CODE -> navigateRoute(PinCodeRoute.GoBack)
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
		navigateRoute(PinCodeRoute.GoEmergency)
	}
	
	private fun goGuardians() {
		navigateRoute(PinCodeRoute.GoGuardians)
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
	
	companion object {
		const val PIN_SIZE = 4
	}
}