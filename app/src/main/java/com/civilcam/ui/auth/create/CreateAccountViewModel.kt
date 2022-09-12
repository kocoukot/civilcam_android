package com.civilcam.ui.auth.create

import androidx.lifecycle.viewModelScope
import com.civilcam.CivilcamApplication.Companion.instance
import com.civilcam.R
import com.civilcam.domainLayer.ServerErrors
import com.civilcam.domainLayer.ServiceException
import com.civilcam.domainLayer.castSafe
import com.civilcam.domainLayer.usecase.auth.GoogleSignInUseCase
import com.civilcam.domainLayer.usecase.auth.SingUpUseCase
import com.civilcam.ext_features.compose.ComposeViewModel
import com.civilcam.ext_features.isEmail
import com.civilcam.ui.auth.create.model.CreateAccountActions
import com.civilcam.ui.auth.create.model.CreateAccountRoute
import com.civilcam.ui.auth.create.model.CreateAccountState
import com.civilcam.ui.auth.create.model.PasswordInputDataType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateAccountViewModel(
    private val singUpUseCase: SingUpUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase,
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
            CreateAccountActions.ClickOkAlert -> clearErrorText()
            CreateAccountActions.FBLogin -> onFacebookSignIn()
            CreateAccountActions.GoogleLogin -> onGoogleSignIn()
        }
    }

    override fun clearErrorText() {
        _state.update { it.copy(alertErrorText = "") }
    }

    private fun confirmPasswordEntered(password: String) {
        _state.update { it.copy(passwordModel = _state.value.passwordModel.copy(confirmPassword = password)) }
    }

    private fun passwordEntered(password: String, meetCriteria: Boolean) {
        _state.update {
            it.copy(
                passwordModel = _state.value.passwordModel.copy(
                    password = password,
                    meetCriteria = meetCriteria
                )
            )
        }
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

    private fun onFacebookSignIn() {
        navigateRoute(CreateAccountRoute.OnFacebookSignIn)
    }

//    fun onFacebookSignedIn(accessToken: String) =
//        viewModelScope.launch {
//            _state.update { it.copy(isLoading = true) }
//            kotlin.runCatching { googleSignInUseCase.invoke(accessToken) }
//                .onSuccess {
////					saveFcmUseCase.saveFcmToken()
//    navigateRoute(CreateAccountRoute.GoSocialsLogin(it))
//                }
//                .onFailure { error ->
//                    error.castSafe<ServiceException>()?.let { castedError ->
//                        _state.update { it.copy(alertError = castedError.errorMessage) }
//                    } ?: run {
//                        _state.update { it.copy(alertError = error.localizedMessage) }
//                    }
//                }
//
//            _state.update { it.copy(isLoading = false) }
//        }

    private fun onGoogleSignIn() {
        navigateRoute(CreateAccountRoute.OnGoogleSignIn)
    }

    fun onGoogleSignedIn(accessToken: String) =
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            kotlin.runCatching { googleSignInUseCase.invoke(accessToken) }
                .onSuccess {
//					saveFcmUseCase.saveFcmToken()
                    navigateRoute(CreateAccountRoute.GoSocialsLogin(it))
                    _state.update { it.copy(isLoading = false) }
                }
                .onFailure { error ->
                    error.castSafe<ServiceException>()?.let { castedError ->
                        _state.update { it.copy(alertErrorText = castedError.errorMessage) }
                    } ?: run {
                        _state.update { it.copy(alertErrorText = error.localizedMessage) }
                    }
                    _state.update { it.copy(isLoading = false) }
                }
        }
}