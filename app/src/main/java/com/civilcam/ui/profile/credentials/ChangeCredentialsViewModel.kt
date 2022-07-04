package com.civilcam.ui.profile.credentials

import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.common.ext.isEmail
import com.civilcam.ui.profile.credentials.model.ChangeCredentialsActions
import com.civilcam.ui.profile.credentials.model.ChangeCredentialsRoute
import com.civilcam.ui.profile.credentials.model.ChangeCredentialsState
import com.civilcam.ui.profile.credentials.model.CredentialType
import kotlinx.coroutines.flow.MutableStateFlow

class ChangeCredentialsViewModel :
	ComposeViewModel<ChangeCredentialsState, ChangeCredentialsRoute, ChangeCredentialsActions>() {
	override var _state: MutableStateFlow<ChangeCredentialsState> = MutableStateFlow(
		ChangeCredentialsState()
	)
	
	override fun setInputActions(action: ChangeCredentialsActions) {
		when (action) {
			ChangeCredentialsActions.ClickSave -> {}
			is ChangeCredentialsActions.EnterInputData -> {
				when(action.dataType) {
					CredentialType.EMAIL -> emailEntered(action.data)
					CredentialType.PHONE -> phoneEntered(action.data)
				}
			}
			ChangeCredentialsActions.CheckCredential -> {
				_state.value =
					_state.value.copy(isEmail = if (_state.value.email.isEmpty()) true else _state.value.email.isEmail())
			}
		}
	}
	
	private fun emailEntered(email: String) {
		_state.value = _state.value.copy(email = email, isEmail = true)
	}
	
	private fun phoneEntered(phone: String) {
		_state.value = _state.value.copy(phone = phone)
	}
}