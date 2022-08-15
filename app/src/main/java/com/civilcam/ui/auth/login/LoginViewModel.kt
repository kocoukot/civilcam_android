package com.civilcam.ui.auth.login

import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.common.ext.isEmail
import com.civilcam.data.network.support.ServerErrors
import com.civilcam.data.network.support.ServiceException
import com.civilcam.domainLayer.usecase.auth.CheckEmailUseCase
import com.civilcam.domainLayer.usecase.auth.SignInUseCase
import com.civilcam.ui.auth.create.model.CreateAccountRoute
import com.civilcam.ui.auth.create.model.PasswordInputDataType
import com.civilcam.ui.auth.login.model.LoginActions
import com.civilcam.ui.auth.login.model.LoginRoute
import com.civilcam.ui.auth.login.model.LoginState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class LoginViewModel(
	private val signInUseCase: SignInUseCase,
	private val checkEmailUseCase: CheckEmailUseCase
) : ComposeViewModel<LoginState, LoginRoute, LoginActions>() {
	
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
					else -> {}
				}
			}
		}
	}
	
	private fun goLogin() {
		_state.update { it.copy(isLoading = true) }
		viewModelScope.launch {
			try {
				if (checkEmailUseCase.checkEmail(_state.value.email)) {
					val response =
						signInUseCase.invoke(
							_state.value.email,
							_state.value.password,
						)
					if (response != null) {
						navigateRoute(LoginRoute.GoLogin(response))
					}
				} else {
					withContext(Dispatchers.Main) {
						_state.update { it.copy(errorText = ServiceException(ServerErrors.USER_NOT_FOUND_BY_EMAIL).errorMessage) }
					}
				}
			} catch (e: ServiceException) {
				withContext(Dispatchers.Main) {
					_state.update { it.copy(errorText = e.errorMessage) }
				}
			}
			_state.update { it.copy(isLoading = false) }
		}
	}
	
	private fun emailEntered(email: String) {
		_state.value = _state.value.copy(email = email, isEmail = email.isEmail())
	}
	
	private fun passwordEntered(password: String) {
		_state.value = _state.value.copy(password = password)
	}
	
	private fun goBack() {
		navigateRoute(LoginRoute.GoBack)
	}
	
	private fun goReset() {
		navigateRoute(LoginRoute.GoReset)
	}
	
	private fun goRegister() {
		navigateRoute(LoginRoute.GoRegister)
	}
}