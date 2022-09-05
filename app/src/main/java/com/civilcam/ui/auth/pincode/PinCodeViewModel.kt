package com.civilcam.ui.auth.pincode

import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.castSafe
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.data.network.support.ServiceException
import com.civilcam.domainLayer.usecase.user.CheckPinUseCase
import com.civilcam.domainLayer.usecase.user.SetPinUseCase
import com.civilcam.ui.auth.pincode.model.PinCodeActions
import com.civilcam.ui.auth.pincode.model.PinCodeFlow
import com.civilcam.ui.auth.pincode.model.PinCodeRoute
import com.civilcam.ui.auth.pincode.model.PinCodeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PinCodeViewModel(
	pinCodeFlow: PinCodeFlow,
	private val checkPinUseCase: CheckPinUseCase,
	private val setPinUseCase: SetPinUseCase
) : ComposeViewModel<PinCodeState, PinCodeRoute, PinCodeActions>() {
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
					setPin(PinCodeFlow.CONFIRM_PIN_CODE)
				} else {
					if (_state.value.confirmPinCode.length == PIN_SIZE) {
						_state.value = _state.value.copy(
							confirmPinCode = "",
							noMatch = true
						)
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
					setPin(PinCodeFlow.CONFIRM_NEW_PIN_CODE)
				} else {
					if (_state.value.confirmPinCode.length == PIN_SIZE) {
						_state.value = _state.value.copy(
							confirmPinCode = "",
							newPinNoMatch = true
						)
					}
				}
			}
			PinCodeFlow.CURRENT_PIN_CODE -> {
				_state.value = _state.value.copy(currentPinCode = pinCode)
				if (_state.value.currentPinCode.length == PIN_SIZE) {
					checkPin(PinCodeFlow.CURRENT_PIN_CODE)
				} else {
					_state.value = _state.value.copy(currentNoMatch = false)
				}
			}
			PinCodeFlow.SOS_PIN_CODE -> {
				_state.value = _state.value.copy(currentPinCode = pinCode)
				if (_state.value.currentPinCode.length == PIN_SIZE) {
					checkPin(PinCodeFlow.SOS_PIN_CODE)
				} else {
					_state.value = _state.value.copy(currentNoMatch = false)
				}
			}
		}
	}
	
	private fun checkPin(pinCodeFlow: PinCodeFlow) {
		viewModelScope.launch {
			kotlin.runCatching {
				checkPinUseCase.checkPin(_state.value.currentPinCode)
			}.onSuccess { response ->
				when (pinCodeFlow) {
					PinCodeFlow.CURRENT_PIN_CODE -> {
						if (response) {
							_state.value = _state.value.copy(
								currentNoMatch = false
							)
							goNewPinCode()
						} else {
							_state.value =
								_state.value.copy(
									currentPinCode = "",
									currentNoMatch = true
								)
						}
					}
					PinCodeFlow.SOS_PIN_CODE -> {
						if (response) {
							_state.value = _state.value.copy(
								currentNoMatch = false
							)
							goEmergency()
						} else {
							_state.value =
								_state.value.copy(
									currentPinCode = "",
									currentNoMatch = true
								)
						}
					}
					else -> {}
				}
			}
				.onFailure { error ->
					error.castSafe<ServiceException>()?.let {
						_state.update { it.copy(errorText = it.errorText) }
					}
				}
		}
	}
	
	private fun setPin(pinCodeFlow: PinCodeFlow) {
		_state.value = _state.value.copy(isLoading = true)
		viewModelScope.launch {
			kotlin.runCatching {
				if (pinCodeFlow == PinCodeFlow.CONFIRM_NEW_PIN_CODE)
					setPinUseCase.setPin(_state.value.currentPinCode, _state.value.pinCode)
				else
					setPinUseCase.setPin(null, _state.value.pinCode)
			}.onSuccess {
				when (pinCodeFlow) {
					PinCodeFlow.CONFIRM_PIN_CODE -> goGuardians()
					PinCodeFlow.CONFIRM_NEW_PIN_CODE -> goUserProfile()
					else -> {}
				}
			}
				.onFailure { error ->
					error.castSafe<ServiceException>()?.let {
						_state.update { it.copy(errorText = it.errorText) }
					}
				}
			_state.value = _state.value.copy(isLoading = false)
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
			PinCodeFlow.CURRENT_PIN_CODE -> navigateRoute(PinCodeRoute.GoBack)
			PinCodeFlow.NEW_PIN_CODE -> _state.value =
				_state.value.copy(screenState = PinCodeFlow.CURRENT_PIN_CODE)
			PinCodeFlow.CONFIRM_NEW_PIN_CODE -> _state.value =
				_state.value.copy(screenState = PinCodeFlow.NEW_PIN_CODE, confirmPinCode = "")
		}
	}
	
	private fun goEmergency() {
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