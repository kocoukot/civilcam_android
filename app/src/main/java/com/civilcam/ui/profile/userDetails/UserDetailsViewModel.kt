package com.civilcam.ui.profile.userDetails

import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.domain.usecase.GetUserInformationUseCase
import com.civilcam.ui.profile.userDetails.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class UserDetailsViewModel(
    private val userId: Int,
    private val getUserInformationUseCase: GetUserInformationUseCase
) : ComposeViewModel<UserDetailsState, UserDetailsRoute, UserDetailsActions>() {

    override var _state: MutableStateFlow<UserDetailsState> = MutableStateFlow(UserDetailsState())

    init {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            kotlin.runCatching { getUserInformationUseCase.getUser("") }
                .onSuccess { user ->
                    _state.value = _state.value.copy(data = user)
                }
                .onFailure {
                    _state.value = _state.value.copy(errorText = it.localizedMessage)
                }
            _state.value = _state.value.copy(isLoading = false)

        }

    }

    override fun setInputActions(action: UserDetailsActions) {
        when (action) {
            UserDetailsActions.ClickGoBack -> goBack()
            UserDetailsActions.ClickGuardenceChange -> changeGuardence()
            UserDetailsActions.ClickStopGuarding -> stopGuarding()
            UserDetailsActions.Mock -> mockAddRequest()
            is UserDetailsActions.ClickRequestAnswer -> requestAnswer(action.isAccepted)
            is UserDetailsActions.ClickShowAlert -> showAlert(action.alertType)
            UserDetailsActions.ClickCloseAlert -> closeAlert()
        }
    }

    private fun goBack() {
        navigateRoute(UserDetailsRoute.GoBack)
    }


    private fun changeGuardence() {
        val data = _state.value.data?.copy()
        data?.let {
            it.isMyGuard = !it.isMyGuard
            _state.value = _state.value.copy(data = data, alertType = null)
        }
    }

    private fun stopGuarding() {
        val data = _state.value.data?.copy()
        data?.let {
            it.guardRequest = null
            _state.value = _state.value.copy(data = data, alertType = null)
        }
    }

    private fun requestAnswer(isAccepted: Boolean) {
        val data = _state.value.data?.copy()
        data?.let {
            if (isAccepted) {
                it.guardRequest = GuardRequest(isGuarding = true)
            } else {
                it.guardRequest = null
            }
            _state.value = _state.value.copy(data = data)
        }


    }

    private fun mockAddRequest() {
        val data = _state.value.data?.copy()
        data?.let {
            it.guardRequest = GuardRequest()
            _state.value = _state.value.copy(data = data)
        }
    }

    private fun showAlert(alertType: StopGuardAlertType) {
        when (alertType) {
            StopGuardAlertType.STOP_GUARDING -> {
                _state.value =
                    _state.value.copy(alertType = alertType)
            }
            StopGuardAlertType.REMOVE_GUARDIAN ->
                if (_state.value.data?.isMyGuard == true) {
                    _state.value = _state.value.copy(alertType = alertType)
                } else {
                    changeGuardence()
                }
        }
    }

    private fun closeAlert() {
        _state.value = _state.value.copy(alertType = null)

    }
}
