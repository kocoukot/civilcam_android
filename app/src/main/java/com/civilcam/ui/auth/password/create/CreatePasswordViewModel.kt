package com.civilcam.ui.auth.password.create

import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.ui.auth.password.create.model.CreatePasswordActions
import com.civilcam.ui.auth.password.create.model.CreatePasswordRoute
import com.civilcam.ui.auth.password.create.model.CreatePasswordState
import kotlinx.coroutines.flow.MutableStateFlow

class CreatePasswordViewModel :
	ComposeViewModel<CreatePasswordState, CreatePasswordRoute, CreatePasswordActions>() {
	override var _state: MutableStateFlow<CreatePasswordState> =
		MutableStateFlow(CreatePasswordState())
	
	override fun setInputActions(action: CreatePasswordActions) {
		when(action) {
		
		}
	}
	
}