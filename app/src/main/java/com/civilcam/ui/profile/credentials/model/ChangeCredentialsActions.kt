package com.civilcam.ui.profile.credentials.model

import com.civilcam.common.ext.compose.ComposeFragmentActions
import com.civilcam.ui.auth.create.model.PasswordInputDataType
import com.civilcam.ui.auth.password.reset.model.ResetActions

sealed class ChangeCredentialsActions : ComposeFragmentActions {
	data class ClickSave(val dataType: CredentialType) : ChangeCredentialsActions()
	object ClickBack : ChangeCredentialsActions()
	object CheckCredential : ChangeCredentialsActions()
	data class EnterInputData(val dataType: CredentialType, val data: String) :
		ChangeCredentialsActions()
}