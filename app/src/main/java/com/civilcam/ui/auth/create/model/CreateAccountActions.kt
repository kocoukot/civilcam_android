package com.civilcam.ui.auth.create.model

import com.civilcam.domainLayer.model.profile.PasswordInputDataType
import com.civilcam.ext_features.compose.ComposeFragmentActions

sealed class CreateAccountActions : ComposeFragmentActions {
	object ClickGoBack : CreateAccountActions()
	object ClickLogin : CreateAccountActions()
	object ClickContinue : CreateAccountActions()
	data class EnterInputData(
		val dataType: PasswordInputDataType,
		val data: String,
		val meetCriteria: Boolean = true
	) : CreateAccountActions()

	object ClickOkAlert : CreateAccountActions()
	object FBLogin : CreateAccountActions()
	object GoogleLogin : CreateAccountActions()
}