package com.civilcam.ui.auth.password.create

import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.ui.auth.create.model.PasswordInputDataType
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
					PasswordInputDataType.PASSWORD -> passwordEntered(action.data)
					PasswordInputDataType.PASSWORD_REPEAT -> confirmPasswordEntered(action.data)
					else -> {}
				}
			}
		}
	}
	
	private fun goBack() {
		navigateRoute(CreatePasswordRoute.GoBack)
	}
	
	private fun goSave() {
		navigateRoute(CreatePasswordRoute.GoSave)
	}
	
	private fun confirmPasswordEntered(password: String) {
		_state.value = _state.value.copy(confirmPassword = password)
	}
	
	private fun passwordEntered(password: String) {
		_state.value = _state.value.copy(password = password)
	}
	
}