package com.civilcam.ui.verification

import android.os.CountDownTimer
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.common.ext.formatTime
import com.civilcam.domain.model.VerificationFlow
import com.civilcam.ui.verification.model.VerificationActions
import com.civilcam.ui.verification.model.VerificationRoute
import com.civilcam.ui.verification.model.VerificationState
import kotlinx.coroutines.flow.MutableStateFlow


class VerificationViewModel(
	verificationFlow: VerificationFlow,
	verificationSubject: String
) :
	ComposeViewModel<VerificationState, VerificationRoute, VerificationActions>() {
	override var _state: MutableStateFlow<VerificationState> = MutableStateFlow(VerificationState())
	
	var timer: CountDownTimer? = null
	
	init {
		getVerificationFlow(verificationFlow)
		getVerificationSubject(verificationSubject)
	}
	
	override fun setInputActions(action: VerificationActions) {
		when (action) {
			is VerificationActions.EnterCodeData -> otpCodeEntered(action.data)
			VerificationActions.ResendClick -> resendClick()
			VerificationActions.ClickGoBack -> goBack()
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
		timer = object : CountDownTimer(60000, 1000) {
			override fun onTick(millisUntilFinished: Long) {
				_state.value = _state.value.copy(timeOut = millisUntilFinished.formatTime())
			}
			
			override fun onFinish() {
				_state.value = _state.value.copy(timeOut = "")
			}
		}.start()
	}
	
	private fun getVerificationFlow(flow: VerificationFlow) {
		_state.value = _state.value.copy(verificationFlow = flow)
	}
	
	private fun getVerificationSubject(subject: String) {
		_state.value = _state.value.copy(verificationSubject = subject)
	}
	
	private fun goToNextPage() {
		timer?.cancel()
		_steps.value = VerificationRoute.ToNextScreen
	}
	
	private fun goBack() {
		_steps.value = VerificationRoute.GoBack
	}
	
	companion object {
		private const val TEST_CODE = "000000"
	}
}