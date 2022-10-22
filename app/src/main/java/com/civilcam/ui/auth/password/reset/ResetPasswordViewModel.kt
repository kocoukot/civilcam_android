package com.civilcam.ui.auth.password.reset

import androidx.lifecycle.viewModelScope
import com.civilcam.domainLayer.model.profile.PasswordInputDataType
import com.civilcam.domainLayer.serviceCast
import com.civilcam.domainLayer.usecase.auth.ResetPasswordUseCase
import com.civilcam.ext_features.compose.ComposeViewModel
import com.civilcam.ext_features.ext.isEmail
import com.civilcam.ui.auth.password.reset.model.ResetActions
import com.civilcam.ui.auth.password.reset.model.ResetRoute
import com.civilcam.ui.auth.password.reset.model.ResetState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResetPasswordViewModel(
	private val resetPasswordUseCase: ResetPasswordUseCase
) : ComposeViewModel<ResetState, ResetRoute, ResetActions>() {
	override var _state: MutableStateFlow<ResetState> = MutableStateFlow(ResetState())
	
	override fun setInputActions(action: ResetActions) {
		when (action) {
			ResetActions.ClickContinue -> goContinue()
			ResetActions.ClickBack -> goBack()
			is ResetActions.EnterInputData -> {
				when (action.dataType) {
					PasswordInputDataType.EMAIL -> emailEntered(action.data)
					else -> {}
				}
			}
			ResetActions.CheckIfEmail -> {
				_state.value =
					_state.value.copy(
						isEmail = if (_state.value.email.isEmpty()) true else _state.value.email.isEmail(),
						errorText = if (_state.value.email.isEmail()) "" else "Invalid email. Please try again. (eg:email@gmail.com)"
					)
			}
		}
	}
	
	private fun goContinue() {
		_state.update { it.copy(isLoading = true) }
		viewModelScope.launch {
			kotlin.runCatching { resetPasswordUseCase.resetPassword(_state.value.email) }
				.onSuccess { response ->
					if (response >= 0) {
						navigateRoute(ResetRoute.GoContinue(_state.value.email))
					}
				}
				.onFailure { error ->
					error.serviceCast { errorMessage, _, isForceLogout ->
						_state.update { it.copy(errorText = errorMessage, emailError = true) }
					}
				}
			_state.update { it.copy(isLoading = false) }
        }
    }

    private fun emailEntered(email: String) {
        _state.value = _state.value.copy(email = email, isEmail = true, emailError = false)
    }

    private fun goBack() {
        navigateRoute(ResetRoute.GoBack)
    }

    override fun clearErrorText() {

    }
}