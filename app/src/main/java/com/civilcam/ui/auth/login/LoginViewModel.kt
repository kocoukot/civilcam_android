package com.civilcam.ui.auth.login

import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.castSafe
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.common.ext.isEmail
import com.civilcam.data.network.support.ServerErrors
import com.civilcam.data.network.support.ServiceException
import com.civilcam.domainLayer.usecase.auth.GoogleSignInUseCase
import com.civilcam.domainLayer.usecase.auth.SignInUseCase
import com.civilcam.ui.auth.create.model.PasswordInputDataType
import com.civilcam.ui.auth.login.model.LoginActions
import com.civilcam.ui.auth.login.model.LoginRoute
import com.civilcam.ui.auth.login.model.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
	private val signInUseCase: SignInUseCase,
	private val googleSignInUseCase: GoogleSignInUseCase,
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
			LoginActions.ClearErrorText -> hideAlert()
			LoginActions.FBLogin -> onFacebookSignIn()
			LoginActions.GoogleLogin -> onGoogleSignIn()
		}
	}
	
	private fun goLogin() {
		_state.update { it.copy(isLoading = true) }
		viewModelScope.launch {
			kotlin.runCatching {
				signInUseCase.invoke(
					_state.value.email,
					_state.value.password,
				)
			}
				.onSuccess {
					navigateRoute(LoginRoute.GoLogin(it))
				}
				.onFailure { error ->
					error.castSafe<ServiceException>()?.let { casted ->
						if (casted.errorCode == ServerErrors.EMAIL_ALREADY_REGISTERED)
							_state.update {
								it.copy(
									credError = true,
									errorText = casted.errorMessage
								)
							}
						else {
							_state.update {
								it.copy(
									emailError = true,
									errorText = casted.errorMessage
								)
							}
						}
					} ?: run {
					}
					
				}
			_state.update { it.copy(isLoading = false) }
		}
	}
	
	private fun emailEntered(email: String) {
		_state.value = _state.value.copy(
			email = email,
			isEmail = email.isEmail(),
			emailError = false,
			credError = false
		)
		if (!email.isEmail()) {
			_state.value = _state.value.copy(errorText = "Invalid email. Please try again. (eg:email@gmail.com)")
		}
	}
	
	private fun passwordEntered(password: String) {
		_state.value = _state.value.copy(password = password, credError = false)
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

	private fun hideAlert() {
		_state.update { it.copy(errorText = "") }
	}

	private fun onFacebookSignIn() {
		navigateRoute(LoginRoute.OnFacebookSignIn)
	}

	fun onFacebookSignedIn(accessToken: String) =
		viewModelScope.launch {
			_state.update { it.copy(isLoading = true) }
			kotlin.runCatching { googleSignInUseCase.invoke(accessToken) }
				.onSuccess {
//					saveFcmUseCase.saveFcmToken()
					navigateRoute(LoginRoute.GoLogin(it))
				}
				.onFailure { error ->
					error.castSafe<ServiceException>()?.let { castedError ->
						_state.update { it.copy(alertError = castedError.errorMessage) }
					} ?: run {
						_state.update { it.copy(alertError = error.localizedMessage) }
					}
				}

			_state.update { it.copy(isLoading = false) }
		}

	private fun onGoogleSignIn() {
		navigateRoute(LoginRoute.OnGoogleSignIn)
	}

	fun onGoogleSignedIn(accessToken: String) =
		viewModelScope.launch {
			_state.update { it.copy(isLoading = true) }
			kotlin.runCatching { googleSignInUseCase.invoke(accessToken) }
				.onSuccess {
//					saveFcmUseCase.saveFcmToken()
					navigateRoute(LoginRoute.GoLogin(it))
				}
				.onFailure { error ->
					error.castSafe<ServiceException>()?.let { castedError ->
						_state.update { it.copy(alertError = castedError.errorMessage) }
					} ?: run {
						_state.update { it.copy(alertError = error.localizedMessage) }
					}
				}

			_state.update { it.copy(isLoading = false) }
		}
}