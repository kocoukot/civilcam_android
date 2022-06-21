package com.civilcam.ui.profile.userDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.civilcam.arch.common.livedata.SingleLiveEvent
import com.civilcam.domain.usecase.GetUserInformationUseCase
import com.civilcam.ui.profile.userDetails.model.GuardRequest
import com.civilcam.ui.profile.userDetails.model.UserDetailsActions
import com.civilcam.ui.profile.userDetails.model.UserDetailsRoute
import com.civilcam.ui.profile.userDetails.model.UserDetailsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserDetailsViewModel(
    private val getUserInformationUseCase: GetUserInformationUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<UserDetailsState> = MutableStateFlow(UserDetailsState())
    val state = _state.asStateFlow()

    private val _steps: SingleLiveEvent<UserDetailsRoute> = SingleLiveEvent()
    val steps: SingleLiveEvent<UserDetailsRoute> = _steps

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

    fun setInputActions(action: UserDetailsActions) {
        when (action) {
            UserDetailsActions.ClickGoBack -> goBack()
            UserDetailsActions.ClickGuardenceChange -> changeGuardence()
            UserDetailsActions.ClickStopGuarding -> stopGuarding()
            UserDetailsActions.Mock -> mockAddRequest()
            is UserDetailsActions.ClickRequestAnswer -> requestAnswer(action.isAccepted)
        }
    }

    private fun goBack() {
        _steps.value = UserDetailsRoute.GoBack
    }


    private fun changeGuardence() {
        val data = _state.value.data?.copy()
        data?.let {
            it.isMyGuard = !it.isMyGuard
            _state.value = _state.value.copy(data = data)
        }
    }

    private fun stopGuarding() {
        val data = _state.value.data?.copy()
        data?.let {
            it.guardRequest = null
            _state.value = _state.value.copy(data = data)
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
}
