package com.civilcam.ui.verification

import android.os.CountDownTimer
import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.common.ext.formatTime
import com.civilcam.data.network.support.ServiceException
import com.civilcam.domainLayer.model.VerificationFlow
import com.civilcam.domainLayer.usecase.verify.SendOtpCodeUseCase
import com.civilcam.domainLayer.usecase.verify.VerifyEmailOtpUseCase
import com.civilcam.ui.verification.model.VerificationActions
import com.civilcam.ui.verification.model.VerificationRoute
import com.civilcam.ui.verification.model.VerificationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class VerificationViewModel(
    private val verificationFlow: VerificationFlow,
    verificationSubject: String,
    private val verifyEmailOtpUseCase: VerifyEmailOtpUseCase,
    private val sendOtpCodeUseCase: SendOtpCodeUseCase,
//	private val verifyResetPasswordOtpUseCase: VerifyResetPasswordOtpUseCase,
) :
    ComposeViewModel<VerificationState, VerificationRoute, VerificationActions>() {
    override var _state: MutableStateFlow<VerificationState> = MutableStateFlow(VerificationState())

    private var timer: CountDownTimer? = null

    init {
        _state.update {
            it.copy(
                verificationFlow = verificationFlow,
                verificationSubject = verificationSubject
            )
        }
        startTimer()
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
                _state.update { it.copy(isLoading = true) }
                viewModelScope.launch {
                    runCatching { verifyEmailOtpUseCase.verifyOtp(verificationFlow, code) }
                        .onSuccess { goToNextPage() }
                        .onFailure { error ->
                            error as ServiceException
                            _state.update { it.copy(errorText = error.errorMessage) }
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
                    error as ServiceException
                    _state.update { it.copy(errorText = error.errorMessage) }
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
        navigateRoute(VerificationRoute.ToNextScreen)
    }

    private fun goBack() {
        navigateRoute(VerificationRoute.GoBack)
    }

    companion object {
        private const val TEST_CODE = "000000"
    }
}