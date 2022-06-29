package com.civilcam.ui.auth.login

import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.common.ext.isEmail
import com.civilcam.ui.auth.create.model.PasswordInputDataType
import com.civilcam.ui.auth.login.model.LoginActions
import com.civilcam.ui.auth.login.model.LoginRoute
import com.civilcam.ui.auth.login.model.LoginState
import kotlinx.coroutines.flow.MutableStateFlow

class LoginViewModel : ComposeViewModel<LoginState, LoginRoute, LoginActions>() {
	
	override var _state: MutableStateFlow<LoginState> = MutableStateFlow(LoginState())
	
	override fun setInputActions(action: LoginActions) {
		when (action) {
			LoginActions.ClickLogin -> goLogin()
			LoginActions.ClickRegister -> goRegister()
			LoginActions.ClickReset -> goReset()
			LoginActions.ClickBack -> goBack()
			is LoginActions.EnterInputData -> {
				when (action.dataType) {
					PasswordInputDataType.EMAIL -> emailEntered(action.data)
					PasswordInputDataType.PASSWORD -> passwordEntered(action.data)
				}
			}
		}
	}
	
	private fun emailEntered(email: String) {
		_state.value = _state.value.copy(email = email, isEmail = email.isEmail())
	}
	
	private fun passwordEntered(password: String) {
		_state.value = _state.value.copy(password = password)
	}
	
	private fun goBack() {
		_steps.value = LoginRoute.GoBack
	}
	
	private fun goLogin() {
		_steps.value = LoginRoute.GoLogin
	}
	
	private fun goReset() {
		_steps.value = LoginRoute.GoReset
	}
	
	private fun goRegister() {
		_steps.value = LoginRoute.GoRegister
	}
}