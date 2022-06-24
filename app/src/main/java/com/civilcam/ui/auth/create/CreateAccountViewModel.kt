package com.civilcam.ui.auth.create

import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.ui.auth.create.model.CreateAccountActions
import com.civilcam.ui.auth.create.model.CreateAccountRoute
import com.civilcam.ui.auth.create.model.CreateAccountState
import kotlinx.coroutines.flow.MutableStateFlow

class CreateAccountViewModel :
	ComposeViewModel<CreateAccountState, CreateAccountRoute, CreateAccountActions>() {
	
	override var _state: MutableStateFlow<CreateAccountState> =
		MutableStateFlow(CreateAccountState())
	
	override fun setInputActions(action: CreateAccountActions) {
		when (action) {
			CreateAccountActions.ClickGoBack -> {}
		}
	}
}