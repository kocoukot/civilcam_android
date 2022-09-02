package com.civilcam.ui.network.inviteByNumber

import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.data.network.support.ServiceException
import com.civilcam.domainLayer.usecase.guardians.InviteByNumberUseCase
import com.civilcam.ui.network.inviteByNumber.model.InviteByNumberActions
import com.civilcam.ui.network.inviteByNumber.model.InviteByNumberModel
import com.civilcam.ui.network.inviteByNumber.model.InviteByNumberRoute
import com.civilcam.ui.network.inviteByNumber.model.InviteByNumberState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class InviteByNumberViewModel(
    private val inviteByNumberUseCase: InviteByNumberUseCase
) : ComposeViewModel<InviteByNumberState, InviteByNumberRoute, InviteByNumberActions>() {
    override var _state: MutableStateFlow<InviteByNumberState> =
        MutableStateFlow(InviteByNumberState())

    override fun setInputActions(action: InviteByNumberActions) {
        when (action) {
            InviteByNumberActions.ClickGoBack -> goBack()
            is InviteByNumberActions.SendInvite -> sendInvite()
            InviteByNumberActions.PhoneCleared -> phoneCleared()
            InviteByNumberActions.ClickCloseScreenAlert -> closeScreenAlert()
            is InviteByNumberActions.PhoneEntered -> phoneEntered(action.phoneNumber)
        }
    }

    private fun goBack() {
        navigateRoute(InviteByNumberRoute.GoBack)
    }

    private fun phoneCleared() {
        _state.update { it.copy(clearNumber = null) }
    }

    private fun phoneEntered(phone: String) {
        _state.update { it.copy(phoneNumber = phone) }
    }

    private fun closeScreenAlert() {
        _state.update { it.copy(errorText = "") }
    }

    private fun sendInvite() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            kotlin.runCatching { inviteByNumberUseCase("+1${_state.value.phoneNumber}") }
                .onSuccess {
                    val numbersList =
                        _state.value.data?.invitationList?.toMutableList() ?: mutableListOf()
                    numbersList.add("+1${_state.value.phoneNumber}")
                    _state.update {
                        it.copy(
                            clearNumber = Unit,
                            phoneNumber = "",
                            data = InviteByNumberModel(invitationList = numbersList.toList())
                        )
                    }
                }
                .onFailure { error ->
                    error as ServiceException
                    _state.update { it.copy(errorText = error.errorMessage) }
                }
                .also {
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }
}