package com.civilcam.ui.profile.userDetails

import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.data.network.support.ServiceException
import com.civilcam.domainLayer.model.GuardRequest
import com.civilcam.domainLayer.model.UserDetailsModel
import com.civilcam.domainLayer.usecase.GetUserInformationUseCase
import com.civilcam.domainLayer.usecase.guardians.AskToGuardUseCase
import com.civilcam.ui.profile.userDetails.model.StopGuardAlertType
import com.civilcam.ui.profile.userDetails.model.UserDetailsActions
import com.civilcam.ui.profile.userDetails.model.UserDetailsRoute
import com.civilcam.ui.profile.userDetails.model.UserDetailsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class UserDetailsViewModel(
    private val userId: Int,
    private val getUserInformationUseCase: GetUserInformationUseCase,
    private val askToGuardUseCase: AskToGuardUseCase
) : ComposeViewModel<UserDetailsState, UserDetailsRoute, UserDetailsActions>() {

    override var _state: MutableStateFlow<UserDetailsState> = MutableStateFlow(UserDetailsState())

    init {
        Timber.i("userDetail id $userId")
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            kotlin.runCatching { getUserInformationUseCase.getUser("") }
                .onSuccess { user ->
                    _state.update { it.copy(data = user) }
                }
                .onFailure { error ->
                    _state.update { it.copy(errorText = error.localizedMessage) }
                }
            _state.update { it.copy(isLoading = false) }

        }
    }

    private fun updateInfo(info: (UserDetailsModel.() -> UserDetailsModel)) {
        _state.update { it.copy(data = info.invoke(getData()), alertType = null) }
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
        _state.update { it.copy(isLoading = true) }
        networkRequest(
            action = { askToGuardUseCase(userId) },
            onSuccess = { updateInfo { copy(isMyGuard = !isMyGuard) } },
            onFailure = { error ->
                error as ServiceException
                _state.update { it.copy(errorText = error.errorMessage) }
            },
            onComplete = { _state.update { it.copy(isLoading = false) } }
        )
    }


    private fun stopGuarding() = updateInfo {
        copy(guardRequest = null)
    }

    private fun requestAnswer(isAccepted: Boolean) = updateInfo {
        copy(guardRequest = if (isAccepted) GuardRequest(isGuarding = true) else null)
    }

    private fun showAlert(alertType: StopGuardAlertType) {
        when (alertType) {
            StopGuardAlertType.STOP_GUARDING -> {
                _state.update { it.copy(alertType = alertType) }
            }
            StopGuardAlertType.REMOVE_GUARDIAN ->
                if (_state.value.data.isMyGuard) {
                    _state.update { it.copy(alertType = alertType) }
                } else {
                    changeGuardence()
                }
        }
    }

    private fun closeAlert() {
        _state.update { it.copy(alertType = null) }
    }

    private fun getData() = _state.value.data.copy()
}
