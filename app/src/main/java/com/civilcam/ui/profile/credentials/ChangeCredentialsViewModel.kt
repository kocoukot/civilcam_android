package com.civilcam.ui.profile.credentials

import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.common.ext.isEmail
import com.civilcam.ui.profile.credentials.model.ChangeCredentialsActions
import com.civilcam.ui.profile.credentials.model.ChangeCredentialsRoute
import com.civilcam.ui.profile.credentials.model.ChangeCredentialsState
import com.civilcam.ui.profile.credentials.model.CredentialType
import kotlinx.coroutines.flow.MutableStateFlow

class ChangeCredentialsViewModel(credentialType: CredentialType) :
	ComposeViewModel<ChangeCredentialsState, ChangeCredentialsRoute, ChangeCredentialsActions>() {
	override var _state: MutableStateFlow<ChangeCredentialsState> = MutableStateFlow(
		ChangeCredentialsState()
	)
	
	init {
		_state.value = _state.value.copy(screenState = credentialType)
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
			CredentialType.PHONE -> _steps.value =
				ChangeCredentialsRoute.GoSave(dataType, _state.value.phone)
			CredentialType.EMAIL -> _steps.value =
				ChangeCredentialsRoute.GoSave(dataType, _state.value.email)
		}
	}
	
	private fun emailEntered(email: String) {
		_state.value = _state.value.copy(email = email, isEmail = true)
	}
	
	private fun phoneEntered(phone: String) {
		_state.value = _state.value.copy(phone = phone)
	}
	
	private fun goBack() {
		_steps.value = ChangeCredentialsRoute.GoBack
	}
}