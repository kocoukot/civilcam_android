package com.civilcam.ui.auth.password

import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.common.ext.isEmail
import com.civilcam.ui.auth.create.model.InputDataType
import com.civilcam.ui.auth.password.model.ResetActions
import com.civilcam.ui.auth.password.model.ResetRoute
import com.civilcam.ui.auth.password.model.ResetState
import kotlinx.coroutines.flow.MutableStateFlow

class ResetPasswordViewModel : ComposeViewModel<ResetState, ResetRoute, ResetActions>() {
	override var _state: MutableStateFlow<ResetState> = MutableStateFlow(ResetState())
	
	override fun setInputActions(action: ResetActions) {
		when (action) {
			ResetActions.ClickContinue -> goContinue()
			ResetActions.ClickBack -> goBack()
			is ResetActions.EnterInputData -> {
				when (action.dataType) {
					InputDataType.EMAIL -> emailEntered(action.data)
				}
			}
		}
	}
	
	private fun emailEntered(email: String) {
		_state.value = _state.value.copy(email = email, isEmail = email.isEmail())
	}
	
	private fun goContinue() {
		_steps.value = ResetRoute.GoContinue(_state.value.email)
	}
	
	private fun goBack() {
		_steps.value = ResetRoute.GoBack
	}
}