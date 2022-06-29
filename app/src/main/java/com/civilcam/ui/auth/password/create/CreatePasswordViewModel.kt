package com.civilcam.ui.auth.password.create

import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.ui.auth.create.model.InputDataType
import com.civilcam.ui.auth.password.create.model.CreatePasswordActions
import com.civilcam.ui.auth.password.create.model.CreatePasswordRoute
import com.civilcam.ui.auth.password.create.model.CreatePasswordState
import kotlinx.coroutines.flow.MutableStateFlow

class CreatePasswordViewModel :
	ComposeViewModel<CreatePasswordState, CreatePasswordRoute, CreatePasswordActions>() {
	override var _state: MutableStateFlow<CreatePasswordState> =
		MutableStateFlow(CreatePasswordState())
	
	override fun setInputActions(action: CreatePasswordActions) {
		when (action) {
			CreatePasswordActions.ClickSave -> goSave()
			CreatePasswordActions.ClickGoBack -> goBack()
			is CreatePasswordActions.EnterInputData -> {
				when (action.dataType) {
					InputDataType.PASSWORD -> passwordEntered(action.data)
					InputDataType.PASSWORD_REPEAT -> confirmPasswordEntered(action.data)
				}
			}
		}
	}
	
	private fun goBack() {
		_steps.value = CreatePasswordRoute.GoBack
	}
	
	private fun goSave() {
		_steps.value = CreatePasswordRoute.GoSave
	}
	
	private fun confirmPasswordEntered(password: String) {
		_state.value = _state.value.copy(confirmPassword = password)
	}
	
	private fun passwordEntered(password: String) {
		_state.value = _state.value.copy(password = password)
	}
	
}