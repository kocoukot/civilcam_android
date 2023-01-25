package com.civilcam.ui.profile.userDetails

import com.civilcam.domainLayer.model.ButtonAnswer
import com.civilcam.domainLayer.model.guard.GuardianStatus
import com.civilcam.domainLayer.model.guard.PersonModel
import com.civilcam.domainLayer.usecase.GetUserDetailUseCase
import com.civilcam.domainLayer.usecase.guardians.AskToGuardUseCase
import com.civilcam.domainLayer.usecase.guardians.DeleteGuardianUseCase
import com.civilcam.domainLayer.usecase.guardians.SetRequestReactionUseCase
import com.civilcam.domainLayer.usecase.guardians.StopGuardingUseCase
import com.civilcam.ext_features.arch.BaseViewModel
import com.civilcam.ext_features.compose.ComposeFragmentActions
import com.civilcam.ui.profile.userDetails.model.StopGuardAlertType
import com.civilcam.ui.profile.userDetails.model.UserDetailsActions
import com.civilcam.ui.profile.userDetails.model.UserDetailsRoute
import com.civilcam.ui.profile.userDetails.model.UserDetailsState
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

class UserDetailsViewModel(
    private val userId: Int,
    private val getUserInformationUseCase: GetUserDetailUseCase,
    private val askToGuardUseCase: AskToGuardUseCase,
    private val setRequestReactionUseCase: SetRequestReactionUseCase,
    private val stopGuardingUseCase: StopGuardingUseCase,
    private val deleteGuardianUseCase: DeleteGuardianUseCase,
) : BaseViewModel.Base<UserDetailsState>(
    mState = MutableStateFlow(UserDetailsState())
) {


    init {
        Timber.i("userDetail id $userId")
        updateInfo { copy(isLoading = true) }
        networkRequest(
            action = { getUserInformationUseCase(userId) },
            onSuccess = { user ->
                Timber.i("userDetail id $user")
                updateInfo { copy(data = user) }
            },
            onFailure = { error -> updateInfo { copy(errorText = error) } },
            onComplete = { updateInfo { copy(isLoading = false) } }
        )
    }

    private fun updateUserInfo(info: (PersonModel.() -> PersonModel)) {
        updateInfo { copy(data = info.invoke(getData()), alertType = null) }
    }


    override fun setInputActions(action: ComposeFragmentActions) {
        when (action) {
            UserDetailsActions.ClickGoBack -> goBack()
            UserDetailsActions.ClickGuardenceChange -> changeGuardence()
            UserDetailsActions.ClickStopGuarding -> stopGuarding()
            is UserDetailsActions.ClickRequestAnswer -> requestAnswer(action.isAccepted)
            is UserDetailsActions.ClickShowAlert -> showAlert(action.alertType)
            UserDetailsActions.ClickCloseAlert -> closeAlert()
            UserDetailsActions.ClickCloseErrorAlert -> clearErrorText()
        }
    }

    override fun clearErrorText() {
        updateInfo { copy(errorText = "") }
    }

    private fun goBack() {
        sendRoute(UserDetailsRoute.GoBack)
    }

    private fun changeGuardence() {
        updateInfo { copy(isLoading = true) }
        networkRequest(
            action = {
                if (getState().data?.outputRequest?.status == GuardianStatus.ACCEPTED)
                    deleteGuardianUseCase(userId)
                else
                    askToGuardUseCase(userId)
            },
            onSuccess = { response ->
                response.outputRequest?.let { outputRequest ->
                    updateUserInfo {
                        copy(
                            isGuardian = response.isGuardian,
                            outputRequest = PersonModel.PersonStatus(
                                outputRequest.statusId,
                                outputRequest.status
                            )
                        )
                    }
                }
            },
            onFailure = { error -> updateInfo { copy(errorText = error) } },
            onComplete = { updateInfo { copy(isLoading = false) } }
        )
    }


    private fun stopGuarding() {
        updateInfo { copy(isLoading = true) }
        networkRequest(
            action = { stopGuardingUseCase(userId) },
            onSuccess = { updateUserInfo { copy(isOnGuard = null) } },
            onFailure = { error -> updateInfo { copy(errorText = error) } },
            onComplete = { updateInfo { copy(isLoading = false) } },
        )
    }

    private fun requestAnswer(isAccepted: ButtonAnswer) {
        getState().data?.inputRequest?.let { status ->
            updateInfo { copy(isLoading = true) }
            networkRequest(
                action = { setRequestReactionUseCase(isAccepted, status.statusId) },
                onSuccess = { response ->
                    updateUserInfo {
                        copy(
                            personPhone = response.personPhone,
                            personAddress = response.personAddress,
                            inputRequest = inputRequest?.copy(status = if (isAccepted.answer) GuardianStatus.ACCEPTED else GuardianStatus.DECLINED),
                            isOnGuard = if (isAccepted.answer) true else isOnGuard
                        )
                    }
                },
                onFailure = { error -> updateInfo { copy(errorText = error) } },
                onComplete = {
                    updateInfo { copy(isLoading = false) }
                },
            )
        }
    }

    private fun showAlert(alertType: StopGuardAlertType) {
        when (alertType) {
            StopGuardAlertType.STOP_GUARDING -> {
                updateInfo { copy(alertType = alertType) }
            }
            StopGuardAlertType.REMOVE_GUARDIAN ->
                if (getState().data?.isGuardian == true
                ) {
                    updateInfo { copy(alertType = alertType) }
                } else {
                    changeGuardence()
                }
        }
    }

    private fun closeAlert() {
        updateInfo { copy(alertType = null) }
    }

    private fun getData() = getState().data?.copy() ?: PersonModel()

}
