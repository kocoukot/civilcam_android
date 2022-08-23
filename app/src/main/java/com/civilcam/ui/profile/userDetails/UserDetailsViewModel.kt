package com.civilcam.ui.profile.userDetails

import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.domainLayer.model.GuardRequest
import com.civilcam.domainLayer.usecase.GetUserInformationUseCase
import com.civilcam.ui.profile.userDetails.model.StopGuardAlertType
import com.civilcam.ui.profile.userDetails.model.UserDetailsActions
import com.civilcam.ui.profile.userDetails.model.UserDetailsRoute
import com.civilcam.ui.profile.userDetails.model.UserDetailsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
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
                    _state.update { it.copy(data = user) }
                }
                .onFailure { error ->
                    _state.update { it.copy(errorText = error.localizedMessage) }
                }
            _state.value = _state.value.copy(isLoading = false)

        }

    }

    override fun setInputActions(action: UserDetailsActions) {
        when (action) {
            UserDetailsActions.ClickGoBack -> goBack()
            UserDetailsActions.ClickGuardenceChange -> changeGuardence()
            UserDetailsActions.ClickStopGuarding -> stopGuarding()
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
        data?.let { dataM ->
            dataM.isMyGuard = !dataM.isMyGuard
            _state.update { it.copy(data = dataM, alertType = null) }
        }
    }

    private fun stopGuarding() {
        val data = _state.value.data?.copy()
        data?.let {
            it.guardRequest = null
            _state.update { state -> state.copy(data = data, alertType = null) }
        }
    }

    private fun requestAnswer(isAccepted: Boolean) {
        val data = _state.value.data?.copy()
        data?.let {
            it.guardRequest = if (isAccepted) GuardRequest(isGuarding = true) else null

            _state.update { state -> state.copy(data = data) }
        }
    }

    private fun showAlert(alertType: StopGuardAlertType) {
        when (alertType) {
            StopGuardAlertType.STOP_GUARDING -> {
                _state.update { it.copy(alertType = alertType) }
            }
            StopGuardAlertType.REMOVE_GUARDIAN ->
                if (_state.value.data?.isMyGuard == true) {
                    _state.update { it.copy(alertType = alertType) }
                } else {
                    changeGuardence()
                }
        }
    }

    private fun closeAlert() {
        _state.update { it.copy(alertType = null) }
    }
}
