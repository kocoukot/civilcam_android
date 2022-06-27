package com.civilcam.ui.auth.create

import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.common.ext.isEmail
import com.civilcam.ui.auth.create.model.CreateAccountActions
import com.civilcam.ui.auth.create.model.CreateAccountRoute
import com.civilcam.ui.auth.create.model.CreateAccountState
import com.civilcam.ui.auth.create.model.InputDataType
import kotlinx.coroutines.flow.MutableStateFlow

class CreateAccountViewModel :
	ComposeViewModel<CreateAccountState, CreateAccountRoute, CreateAccountActions>() {
	
	override var _state: MutableStateFlow<CreateAccountState> =
		MutableStateFlow(CreateAccountState())
	
	override fun setInputActions(action: CreateAccountActions) {
		when (action) {
			CreateAccountActions.ClickGoBack -> {
				goBack()
			}
			CreateAccountActions.ClickLogin -> {
				goLogin()
			}
			CreateAccountActions.ClickContinue -> {
				goContinue()
			}
			is CreateAccountActions.EnterInputData -> {
				when(action.dataType) {
					InputDataType.EMAIL -> { emailEntered(action.data) }
					InputDataType.PASSWORD -> { passwordEntered(action.data) }
					InputDataType.PASSWORD_REPEAT -> { confirmPasswordEntered(action.data) }
				}
			}
		}
	}
	
	private fun confirmPasswordEntered(password: String) {
		_state.value = _state.value.copy(confirmPassword = password)
	}
	
	private fun passwordEntered(password: String) {
		_state.value = _state.value.copy(password = password)
	}
	
	private fun emailEntered(email: String) {
		_state.value = _state.value.copy(email = email, isEmail = email.isEmail())
	}
	
	private fun goContinue() {
		_steps.value = CreateAccountRoute.GoContinue(_state.value.email)
	}
	
	private fun goLogin() {
		_steps.value = CreateAccountRoute.GoLogin
	}
	
	private fun goBack() {
		_steps.value = CreateAccountRoute.GoBack
	}
}