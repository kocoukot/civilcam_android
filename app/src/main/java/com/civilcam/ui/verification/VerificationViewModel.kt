package com.civilcam.ui.verification

import android.os.CountDownTimer
import androidx.lifecycle.viewModelScope
import com.civilcam.domainLayer.model.VerificationFlow
import com.civilcam.domainLayer.serviceCast
import com.civilcam.domainLayer.usecase.auth.VerifyResetPasswordOtpUseCase
import com.civilcam.domainLayer.usecase.verify.SendOtpCodeUseCase
import com.civilcam.domainLayer.usecase.verify.VerifyEmailOtpUseCase
import com.civilcam.ext_features.compose.ComposeViewModel
import com.civilcam.ext_features.ext.formatTime
import com.civilcam.ui.verification.model.VerificationActions
import com.civilcam.ui.verification.model.VerificationRoute
import com.civilcam.ui.verification.model.VerificationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VerificationViewModel(
	private val verificationFlow: VerificationFlow,
	private val verificationSubject: String,
	private val newSubject: String,
	private val verifyEmailOtpUseCase: VerifyEmailOtpUseCase,
	private val sendOtpCodeUseCase: SendOtpCodeUseCase,
	private val verifyResetPasswordOtpUseCase: VerifyResetPasswordOtpUseCase,
) :
	ComposeViewModel<VerificationState, VerificationRoute, VerificationActions>() {
	override var _state: MutableStateFlow<VerificationState> = MutableStateFlow(VerificationState())

	private var timer: CountDownTimer? = null

	init {
		_state.update {
			it.copy(
				verificationFlow = verificationFlow,
				verificationSubject = verificationSubject,
				newSubject = newSubject
			)
		}
		startTimer()
	}

	override fun setInputActions(action: VerificationActions) {
		when (action) {
			is VerificationActions.EnterCodeData -> otpCodeEntered(action.data)
			VerificationActions.ResendClick -> resendClick()
			VerificationActions.ClickGoBack -> goBack()
			VerificationActions.ClickCloseAlert -> clearErrorText()
		}
	}

	private fun otpCodeEntered(code: String) {
		code.takeIf { code.length == 6 }
			?.let {
				_state.update { it.copy(isLoading = true) }
				viewModelScope.launch {
					runCatching {
						if (verificationFlow == VerificationFlow.RESET_PASSWORD) {
							verifyResetPasswordOtpUseCase.verifyOtp(
								_state.value.verificationSubject,
								code
							)
						} else {
							verifyEmailOtpUseCase.verifyOtp(verificationFlow, code)
						}
					}
						.onSuccess {
							if (verificationFlow == VerificationFlow.RESET_PASSWORD) {
								_state.value = _state.value.copy(token = it.toString())
							}
							goToNextPage()

						}
						.onFailure { error ->
							error.serviceCast { msg, _, isForceLogout ->
								_state.update { it.copy(errorText = msg) }
							}
						}
					_state.update { it.copy(isLoading = false) }

				}
			}
		if (code.length < 6) _state.update { it.copy(hasError = false) }
	}

	private fun resendClick() {
		viewModelScope.launch {
			kotlin.runCatching { sendOtpCodeUseCase.invoke(verificationFlow) }
				.onSuccess { startTimer() }
				.onFailure { error ->
					error.serviceCast { msg, _, isForceLogout ->
						_state.update { it.copy(errorText = msg) }
					}
				}
		}
	}

	private fun startTimer() {
		viewModelScope.launch {
			timer = object : CountDownTimer(60000, 1000) {
				override fun onTick(millisUntilFinished: Long) {
					_state.update { it.copy(timeOut = millisUntilFinished.formatTime()) }
				}

				override fun onFinish() {
					_state.update { it.copy(timeOut = "") }
				}
			}.start()
		}
	}

	private fun goToNextPage() {
		timer?.cancel()
        if (verificationFlow == VerificationFlow.RESET_PASSWORD) {
			navigateRoute(VerificationRoute.GoPasswordCreate(_state.value.token))
		} else {
			navigateRoute(VerificationRoute.ToNextScreen)
		}
	}

	private fun goBack() {
		navigateRoute(VerificationRoute.GoBack)
	}

	override fun clearErrorText() {
		_state.update { it.copy(errorText = "") }
	}
}