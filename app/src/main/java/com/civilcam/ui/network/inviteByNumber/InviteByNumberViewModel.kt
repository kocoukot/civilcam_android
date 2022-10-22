package com.civilcam.ui.network.inviteByNumber

import androidx.lifecycle.viewModelScope
import com.civilcam.domainLayer.model.guard.GuardianStatus
import com.civilcam.domainLayer.serviceCast
import com.civilcam.domainLayer.usecase.guardians.GetPhoneInvitesUseCase
import com.civilcam.domainLayer.usecase.guardians.InviteByNumberUseCase
import com.civilcam.ext_features.compose.ComposeViewModel
import com.civilcam.ext_features.ext.clearPhone
import com.civilcam.ui.network.inviteByNumber.model.InviteByNumberActions
import com.civilcam.ui.network.inviteByNumber.model.InviteByNumberModel
import com.civilcam.ui.network.inviteByNumber.model.InviteByNumberRoute
import com.civilcam.ui.network.inviteByNumber.model.InviteByNumberState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class InviteByNumberViewModel(
    private val inviteByNumberUseCase: InviteByNumberUseCase,
    private val getPhoneInvitesUseCase: GetPhoneInvitesUseCase
) : ComposeViewModel<InviteByNumberState, InviteByNumberRoute, InviteByNumberActions>() {
    override var _state: MutableStateFlow<InviteByNumberState> =
        MutableStateFlow(InviteByNumberState())

    init {
        _state.update { it.copy(isLoading = true) }
        networkRequest(
            action = { getPhoneInvitesUseCase() },
            onSuccess = { list ->
                _state.update {
                    it.copy(data =
                    InviteByNumberModel(
                        list.filter { contact -> contact.status == GuardianStatus.PENDING }
                            .map { contact -> "+${contact.phone.clearPhone()}" })
                    )
                }
            },
            onFailure = { error ->
                error.serviceCast { msg, _, isForceLogout -> _state.update { it.copy(errorText = msg) } }
            },
            onComplete = { _state.update { it.copy(isLoading = false) } },
        )
    }


    override fun setInputActions(action: InviteByNumberActions) {
        when (action) {
            InviteByNumberActions.ClickGoBack -> goBack()
            is InviteByNumberActions.SendInvite -> sendInvite()
            InviteByNumberActions.PhoneCleared -> phoneCleared()
            InviteByNumberActions.ClickCloseScreenAlert -> clearErrorText()
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

    override fun clearErrorText() {
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
                    error.serviceCast { msg, _, isForceLogout -> _state.update { it.copy(errorText = msg) } }
                }
                .also {
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }
}