package com.civilcam.ui.profile.credentials

import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.common.ext.isEmail
import com.civilcam.data.network.support.ServiceException
import com.civilcam.domainLayer.usecase.profile.ChangePhoneNumberUseCase
import com.civilcam.domainLayer.usecase.user.ChangeEmailUseCase
import com.civilcam.ui.profile.credentials.model.ChangeCredentialsActions
import com.civilcam.ui.profile.credentials.model.ChangeCredentialsRoute
import com.civilcam.ui.profile.credentials.model.ChangeCredentialsState
import com.civilcam.ui.profile.userProfile.model.UserProfileType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChangeCredentialsViewModel(
    credentialType: UserProfileType,
    credential: String,
    private val changePhoneNumberUseCase: ChangePhoneNumberUseCase,
    private val changeEmailUseCase: ChangeEmailUseCase
) : ComposeViewModel<ChangeCredentialsState, ChangeCredentialsRoute, ChangeCredentialsActions>() {
	override var _state: MutableStateFlow<ChangeCredentialsState> = MutableStateFlow(
		ChangeCredentialsState()
	)

	init {
        _state.update { it.copy(screenState = credentialType, currentEmail = credential) }
	}

    override fun setInputActions(action: ChangeCredentialsActions) {
        when (action) {
            is ChangeCredentialsActions.ClickSave -> goSave(action.dataType)
            ChangeCredentialsActions.ClickBack -> goBack()
            ChangeCredentialsActions.CheckCredential ->
                _state.update { it.copy(isEmail = if (_state.value.email.isEmpty()) true else _state.value.email.isEmail()) }
            is ChangeCredentialsActions.EnteredEmail -> emailEntered(action.data)
            is ChangeCredentialsActions.EnteredPhone -> phoneEntered(action.data)
        }
    }

    private fun goSave(dataType: UserProfileType) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            kotlin.runCatching {
                when (dataType) {
                    UserProfileType.PHONE_NUMBER -> changePhoneNumberUseCase.invoke(_state.value.phone)
                    else -> changeEmailUseCase.changeEmail(
                        _state.value.currentEmail,
                        _state.value.email
                    )
                }
            }.onSuccess {
                navigateRoute(
                    ChangeCredentialsRoute.GoSave(
                        dataType,
                        when (dataType) {
                            UserProfileType.PHONE_NUMBER -> _state.value.phone
                            else -> _state.value.email
                        },
                        if (dataType == UserProfileType.EMAIL) _state.value.currentEmail else ""
                    )
                )
            }
                .onFailure { error ->
                    error as ServiceException
                    if (error.isForceLogout) navigateRoute(ChangeCredentialsRoute.ForceLogout)
                    _state.update { it.copy(errorText = error.errorMessage, phoneError = true) }
                }
        }
    }

    private fun emailEntered(email: String) {
        _state.update { it.copy(email = email, isEmail = true, emailError = false) }
    }

    private fun phoneEntered(phone: String) {
        _state.update { it.copy(phone = phone, phoneError = false) }
	}

    private fun goBack() {
		navigateRoute(ChangeCredentialsRoute.GoBack)
	}
}