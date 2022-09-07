package com.civilcam.ui.profile.userDetails

import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.common.ext.serviceCast
import com.civilcam.domainLayer.model.ButtonAnswer
import com.civilcam.domainLayer.model.guard.GuardianStatus
import com.civilcam.domainLayer.model.guard.PersonModel
import com.civilcam.domainLayer.usecase.GetUserDetailUseCase
import com.civilcam.domainLayer.usecase.guardians.AskToGuardUseCase
import com.civilcam.domainLayer.usecase.guardians.DeleteGuardianUseCase
import com.civilcam.domainLayer.usecase.guardians.SetRequestReactionUseCase
import com.civilcam.domainLayer.usecase.guardians.StopGuardingUseCase
import com.civilcam.ui.profile.userDetails.model.StopGuardAlertType
import com.civilcam.ui.profile.userDetails.model.UserDetailsActions
import com.civilcam.ui.profile.userDetails.model.UserDetailsRoute
import com.civilcam.ui.profile.userDetails.model.UserDetailsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber

class UserDetailsViewModel(
    private val userId: Int,
    private val getUserInformationUseCase: GetUserDetailUseCase,
    private val askToGuardUseCase: AskToGuardUseCase,
    private val setRequestReactionUseCase: SetRequestReactionUseCase,
    private val stopGuardingUseCase: StopGuardingUseCase,
    private val deleteGuardianUseCase: DeleteGuardianUseCase,
) : ComposeViewModel<UserDetailsState, UserDetailsRoute, UserDetailsActions>() {

    override var _state: MutableStateFlow<UserDetailsState> = MutableStateFlow(UserDetailsState())

    init {
        Timber.i("userDetail id $userId")
        _state.update { it.copy(isLoading = true) }
        networkRequest(
            action = { getUserInformationUseCase(userId) },
            onSuccess = { user -> _state.update { it.copy(data = user) } },
            onFailure = { error ->
                error.serviceCast { msg, _, isForceLogout ->
                    _state.update { it.copy(errorText = msg) }
                }
            },
            onComplete = { _state.update { it.copy(isLoading = false) } }
        )
    }

    private fun updateInfo(info: (PersonModel.() -> PersonModel)) {
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
            UserDetailsActions.ClickCloseErrorAlert -> closeErrorAlert()
        }
    }

    private fun closeErrorAlert() {
        _state.update { it.copy(errorText = "") }
    }

    private fun goBack() {
        navigateRoute(UserDetailsRoute.GoBack)
    }

    private fun changeGuardence() {
        _state.update { it.copy(isLoading = true) }
        networkRequest(
            action = {
                if (_state.value.data?.isGuardian == true)
                    deleteGuardianUseCase(userId)
                else
                    askToGuardUseCase(userId)
            },
            onSuccess = { updateInfo { copy(isGuardian = !isGuardian) } },
            onFailure = { error ->
                error.serviceCast { msg, _, isForceLogout ->
                    _state.update { it.copy(errorText = msg) }
                }
            },
            onComplete = { _state.update { it.copy(isLoading = false) } }
        )
    }


    private fun stopGuarding() {
        _state.update { it.copy(isLoading = true) }
        networkRequest(
            action = { stopGuardingUseCase(userId) },
            onSuccess = { updateInfo { copy(isOnGuard = null) } },
            onFailure = { error ->
                error.serviceCast { msg, _, isForceLogout ->
                    _state.update { it.copy(errorText = msg) }
                }
            },
            onComplete = { _state.update { it.copy(isLoading = false) } },
        )
    }

    private fun requestAnswer(isAccepted: ButtonAnswer) {
        _state.value.data?.personStatus?.let { status ->
            _state.update { it.copy(isLoading = true) }
            networkRequest(
                action = { setRequestReactionUseCase(isAccepted, status.statusId) },
                onSuccess = { response ->
                    updateInfo {
                        copy(
                            personPhone = response.personPhone,
                            personAddress = response.personAddress,
                            personStatus = personStatus?.copy(status = if (isAccepted.answer) GuardianStatus.ACCEPTED else GuardianStatus.DECLINED),
                            isOnGuard = if (isAccepted.answer) true else isOnGuard
                        )
                    }
                },
                onFailure = { error ->
                    error.serviceCast { msg, _, isForceLogout ->
                        _state.update { it.copy(errorText = msg) }
                    }
                },
                onComplete = {
                    _state.update { it.copy(isLoading = false) }
                },
            )
        }
    }

    private fun showAlert(alertType: StopGuardAlertType) {
        when (alertType) {
            StopGuardAlertType.STOP_GUARDING -> {
                _state.update { it.copy(alertType = alertType) }
            }
            StopGuardAlertType.REMOVE_GUARDIAN ->
                if (_state.value.data?.isGuardian == true) {
                    _state.update { it.copy(alertType = alertType) }
                } else {
                    changeGuardence()
                }
        }
    }

    private fun closeAlert() {
        _state.update { it.copy(alertType = null) }
    }

    private fun getData() = _state.value.data?.copy() ?: PersonModel()
}
