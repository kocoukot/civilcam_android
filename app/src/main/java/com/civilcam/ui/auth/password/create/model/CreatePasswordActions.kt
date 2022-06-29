package com.civilcam.ui.auth.password.create.model

import com.civilcam.common.ext.compose.ComposeFragmentActions
import com.civilcam.ui.auth.create.model.PasswordInputDataType

sealed class CreatePasswordActions : ComposeFragmentActions {
	object ClickGoBack : CreatePasswordActions()
	object ClickSave : CreatePasswordActions()
	data class EnterInputData(val dataType: PasswordInputDataType, val data: String) :
		CreatePasswordActions()
}