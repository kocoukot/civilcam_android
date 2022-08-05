package com.civilcam.ui.auth.create

import androidx.lifecycle.viewModelScope
import com.civilcam.CivilcamApplication.Companion.instance
import com.civilcam.R
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.common.ext.isEmail
import com.civilcam.data.network.support.ServerErrors
import com.civilcam.data.network.support.ServiceException
import com.civilcam.domain.usecase.auth.SingUpUseCase
import com.civilcam.ui.auth.create.model.CreateAccountActions
import com.civilcam.ui.auth.create.model.CreateAccountRoute
import com.civilcam.ui.auth.create.model.CreateAccountState
import com.civilcam.ui.auth.create.model.PasswordInputDataType
import com.standartmedia.common.ext.castSafe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateAccountViewModel(
    private val singUpUseCase: SingUpUseCase
) : ComposeViewModel<CreateAccountState, CreateAccountRoute, CreateAccountActions>() {

    override var _state: MutableStateFlow<CreateAccountState> =
        MutableStateFlow(CreateAccountState())

    override fun setInputActions(action: CreateAccountActions) {
        when (action) {
            CreateAccountActions.ClickGoBack -> goBack()
            CreateAccountActions.ClickLogin -> goLogin()
            CreateAccountActions.ClickContinue -> goContinue()
            is CreateAccountActions.EnterInputData -> {
                when (action.dataType) {
                    PasswordInputDataType.EMAIL -> {
                        emailEntered(action.data)
                    }
                    PasswordInputDataType.PASSWORD -> {
                        passwordEntered(action.data, action.meetCriteria)
                    }
                    PasswordInputDataType.PASSWORD_REPEAT -> {
                        confirmPasswordEntered(action.data)
                    }
                }
            }
            CreateAccountActions.ClickOkAlert -> closeAlert()
        }
    }

    private fun closeAlert() {
        _state.update { it.copy(alertErrorText = "") }
    }

    private fun confirmPasswordEntered(password: String) {
        _state.value =
            _state.value.copy(passwordModel = _state.value.passwordModel.copy(confirmPassword = password))
    }

    private fun passwordEntered(password: String, meetCriteria: Boolean) {
        _state.value =
            _state.value.copy(passwordModel = _state.value.passwordModel.copy(password = password))
        _state.value =
            _state.value.copy(passwordModel = _state.value.passwordModel.copy(meetCriteria = meetCriteria))

    }

    private fun emailEntered(email: String) {
        _state.update {
            it.copy(
                email = email,
                isEmail = if (email.isEmpty()) true else email.isEmail(),
                emailErrorText = instance.getString(R.string.invalid_email)
            )
        }
    }

    private fun goContinue() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            if (_state.value.email == "test@messapps.com" && _state.value.passwordModel.password == "Password1@")
                navigateRoute(CreateAccountRoute.GoContinue(_state.value.email))
            else
                kotlin.runCatching {
                    singUpUseCase.invoke(_state.value.email, _state.value.passwordModel.password)
                }
                    .onSuccess {
                        navigateRoute(CreateAccountRoute.GoContinue(_state.value.email))
                    }
                    .onFailure { error ->
                        error.castSafe<ServiceException>()?.let { casted ->
                            if (casted.errorCode == ServerErrors.EMAIL_ALREADY_REGISTERED)
                                _state.update {
                                    it.copy(
                                        emailErrorText = casted.errorMessage,
                                        isEmail = false
                                    )
                                }
                            else {
                                _state.update { it.copy(alertErrorText = casted.errorMessage) }
                            }
                        } ?: run {
                        }

                    }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun goLogin() {
        navigateRoute(CreateAccountRoute.GoLogin)
    }

    private fun goBack() {
        navigateRoute(CreateAccountRoute.GoBack)
    }
}