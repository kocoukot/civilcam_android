package com.civilcam.ui.auth.password.reset.model

import com.civilcam.common.ext.compose.ComposeFragmentActions
import com.civilcam.ui.auth.create.model.PasswordInputDataType

sealed class ResetActions : ComposeFragmentActions {
	object ClickBack : ResetActions()
	object ClickContinue : ResetActions()
	object CheckIfEmail : ResetActions()
	data class EnterInputData(val dataType: PasswordInputDataType, val data: String) :
		ResetActions()
}