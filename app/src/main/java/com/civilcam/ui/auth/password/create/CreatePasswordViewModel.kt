package com.civilcam.ui.auth.password.create

import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.serviceCast
import com.civilcam.domainLayer.usecase.auth.RecoverPasswordUseCase
import com.civilcam.ext_features.compose.ComposeViewModel
import com.civilcam.ui.auth.create.model.PasswordInputDataType
import com.civilcam.ui.auth.password.create.model.CreatePasswordActions
import com.civilcam.ui.auth.password.create.model.CreatePasswordRoute
import com.civilcam.ui.auth.password.create.model.CreatePasswordState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreatePasswordViewModel(
	token: String,
	private val recoverPasswordUseCase: RecoverPasswordUseCase
) :
	ComposeViewModel<CreatePasswordState, CreatePasswordRoute, CreatePasswordActions>() {
	override var _state: MutableStateFlow<CreatePasswordState> =
		MutableStateFlow(CreatePasswordState())
	
	init {
		_state.value = _state.value.copy(token = token)
	}
	
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
		_state.update { it.copy(isLoading = true) }
		viewModelScope.launch {
			kotlin.runCatching {
				recoverPasswordUseCase.recoverPassword(
					_state.value.token,
					_state.value.password
				)
			}
				.onSuccess { response ->
					if (response) {
						navigateRoute(CreatePasswordRoute.GoSave)
					}
				}
				.onFailure { error ->
					error.serviceCast { msg, _, isForceLogout -> _state.update { it.copy(errorText = msg) } }
				}
			_state.update { it.copy(isLoading = false) }
        }
    }

    private fun confirmPasswordEntered(password: String) {
        _state.value = _state.value.copy(confirmPassword = password)
    }

    private fun passwordEntered(password: String) {
        _state.value = _state.value.copy(password = password)
    }

    override fun clearErrorText() {

    }

}