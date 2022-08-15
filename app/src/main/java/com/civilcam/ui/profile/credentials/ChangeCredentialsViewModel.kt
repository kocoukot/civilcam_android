package com.civilcam.ui.profile.credentials

import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.common.ext.isEmail
import com.civilcam.data.network.support.ServiceException
import com.civilcam.domainLayer.usecase.auth.CheckEmailUseCase
import com.civilcam.domainLayer.usecase.profile.ChangePhoneNumberUseCase
import com.civilcam.domainLayer.usecase.user.ChangeEmailUseCase
import com.civilcam.ui.profile.credentials.model.ChangeCredentialsActions
import com.civilcam.ui.profile.credentials.model.ChangeCredentialsRoute
import com.civilcam.ui.profile.credentials.model.ChangeCredentialsState
import com.civilcam.ui.profile.credentials.model.CredentialType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChangeCredentialsViewModel(
	credentialType: CredentialType,
	credential: String,
	private val changePhoneNumberUseCase: ChangePhoneNumberUseCase,
	private val changeEmailUseCase: ChangeEmailUseCase
) : ComposeViewModel<ChangeCredentialsState, ChangeCredentialsRoute, ChangeCredentialsActions>() {
	override var _state: MutableStateFlow<ChangeCredentialsState> = MutableStateFlow(
		ChangeCredentialsState()
	)
	
	init {
		_state.value = _state.value.copy(screenState = credentialType)
		_state.value = _state.value.copy(currentEmail = credential)
	}
	
	override fun setInputActions(action: ChangeCredentialsActions) {
		when (action) {
			is ChangeCredentialsActions.ClickSave -> goSave(action.dataType)
			is ChangeCredentialsActions.EnterInputData -> {
				when (action.dataType) {
					CredentialType.EMAIL -> emailEntered(action.data)
					CredentialType.PHONE -> phoneEntered(action.data)
				}
			}
			ChangeCredentialsActions.ClickBack -> goBack()
			ChangeCredentialsActions.CheckCredential -> {
				_state.value =
					_state.value.copy(isEmail = if (_state.value.email.isEmpty()) true else _state.value.email.isEmail())
			}
		}
	}
	
	private fun goSave(dataType: CredentialType) {
		when (dataType) {
			CredentialType.PHONE ->
				viewModelScope.launch {
					kotlin.runCatching { changePhoneNumberUseCase.invoke(_state.value.phone) }
						.onSuccess {
							navigateRoute(
								ChangeCredentialsRoute.GoSave(
									dataType,
									_state.value.phone
								)
							)
						}
						.onFailure { error ->
							error as ServiceException
							_state.update { it.copy(errorText = error.errorMessage, phoneError = true) }
						}
				}
			CredentialType.EMAIL ->
				viewModelScope.launch {
					kotlin.runCatching {
						changeEmailUseCase.changeEmail(
							_state.value.currentEmail,
							_state.value.email
						)
					}
						.onSuccess {
							navigateRoute(
								ChangeCredentialsRoute.GoSave(
									dataType,
									_state.value.email
								)
							)
						}
						.onFailure { error ->
							error as ServiceException
							_state.update { it.copy(errorText = error.errorMessage, emailError = true) }
						}
				}
		}
	}
	
	private fun emailEntered(email: String) {
		_state.value = _state.value.copy(email = email, isEmail = true, emailError = false)
	}
	
	private fun phoneEntered(phone: String) {
		_state.value = _state.value.copy(phone = phone, phoneError = false)
	}
	
	private fun goBack() {
		navigateRoute(ChangeCredentialsRoute.GoBack)
	}
}