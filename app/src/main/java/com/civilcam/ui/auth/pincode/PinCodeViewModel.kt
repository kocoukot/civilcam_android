package com.civilcam.ui.auth.pincode

import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.ui.auth.pincode.model.PinCodeActions
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
			PinCodeActions.GoGuardians -> {}
		}
	}
	
	private fun goBack() {
		_steps.value = PinCodeRoute.GoBack
	}
}