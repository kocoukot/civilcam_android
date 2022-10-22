package com.civilcam.ui.auth.password.create.model

import com.civilcam.domainLayer.model.profile.PasswordInputDataType
import com.civilcam.ext_features.compose.ComposeFragmentActions

sealed class CreatePasswordActions : ComposeFragmentActions {
	object ClickGoBack : CreatePasswordActions()
	object ClickSave : CreatePasswordActions()
	data class EnterInputData(val dataType: PasswordInputDataType, val data: String) :
		CreatePasswordActions()
}