package com.civilcam.ui.auth.create.model

import com.civilcam.common.ext.compose.ComposeFragmentActions

sealed class CreateAccountActions : ComposeFragmentActions {
	object ClickGoBack : CreateAccountActions()
	object ClickLogin : CreateAccountActions()
	object ClickContinue : CreateAccountActions()
	data class EnterInputData(val dataType: InputDataType, val data: String) : CreateAccountActions()
}