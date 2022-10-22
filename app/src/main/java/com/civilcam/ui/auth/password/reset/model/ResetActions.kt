package com.civilcam.ui.auth.password.reset.model

import com.civilcam.domainLayer.model.profile.PasswordInputDataType
import com.civilcam.ext_features.compose.ComposeFragmentActions

sealed class ResetActions : ComposeFragmentActions {
	object ClickBack : ResetActions()
	object ClickContinue : ResetActions()
	object CheckIfEmail : ResetActions()
	data class EnterInputData(val dataType: PasswordInputDataType, val data: String) :
		ResetActions()
}