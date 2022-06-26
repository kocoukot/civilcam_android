package com.civilcam.ui.verification

import android.os.CountDownTimer
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.common.ext.formatTime
import com.civilcam.ui.verification.model.VerificationActions
import com.civilcam.ui.verification.model.VerificationRoute
import com.civilcam.ui.verification.model.VerificationState
import kotlinx.coroutines.flow.MutableStateFlow


class VerificationViewModel :
	ComposeViewModel<VerificationState, VerificationRoute, VerificationActions>() {
	override var _state: MutableStateFlow<VerificationState> = MutableStateFlow(VerificationState())
	
	override fun setInputActions(action: VerificationActions) {
		when (action) {
			is VerificationActions.EnterCodeData -> otpCodeEntered(action.data)
			VerificationActions.ResendClick -> resendClick()
		}
	}
	
	private fun otpCodeEntered(code: String) {
		code.takeIf { code.length == 6 }
			?.let {
				_state.value = _state.value.copy(hasError = it != TEST_CODE)
				if (it == TEST_CODE) goToNextPage()
			}
		if (code.length < 6) _state.value = _state.value.copy(hasError = false)
	}
	
	private fun resendClick() {
		startTimer()
	}
	
	private fun startTimer() {
		object : CountDownTimer(60000, 1000) {
			override fun onTick(millisUntilFinished: Long) {
				_state.value = _state.value.copy(timeOut = millisUntilFinished.formatTime())
			}
			
			override fun onFinish() {
				_state.value = _state.value.copy(timeOut = "")
			}
		}.start()
	}
	
	private fun goToNextPage() {
		_steps.value = VerificationRoute.ToNextScreen
	}
	
	companion object {
		private const val TEST_CODE = "000000"
	}
}