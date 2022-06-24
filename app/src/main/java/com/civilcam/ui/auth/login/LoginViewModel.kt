package com.civilcam.ui.auth.login

import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.ui.auth.login.model.LoginActions
import com.civilcam.ui.auth.login.model.LoginRoute
import com.civilcam.ui.auth.login.model.LoginState
import kotlinx.coroutines.flow.MutableStateFlow

class LoginViewModel : ComposeViewModel<LoginState, LoginRoute, LoginActions>() {
	
	override var _state: MutableStateFlow<LoginState> = MutableStateFlow(LoginState())
	
	
	override fun setInputActions(action: LoginActions) {
		TODO("Not yet implemented")
	}
	
	private fun goBack() {
		_steps.value = LoginRoute.GoBack
	}
}