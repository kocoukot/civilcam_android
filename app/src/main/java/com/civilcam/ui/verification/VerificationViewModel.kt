package com.civilcam.ui.verification

import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.ui.verification.model.VerificationActions
import com.civilcam.ui.verification.model.VerificationRoute
import com.civilcam.ui.verification.model.VerificationState
import kotlinx.coroutines.flow.MutableStateFlow

class VerificationViewModel :
	ComposeViewModel<VerificationState, VerificationRoute, VerificationActions>() {
	override var _state: MutableStateFlow<VerificationState> = MutableStateFlow(VerificationState())
	
	override fun setInputActions(action: VerificationActions) {
	
	}
}