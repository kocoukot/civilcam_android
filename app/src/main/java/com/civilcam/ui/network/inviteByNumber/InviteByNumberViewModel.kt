package com.civilcam.ui.network.inviteByNumber

import androidx.lifecycle.viewModelScope
import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.ui.network.inviteByNumber.model.InviteByNumberActions
import com.civilcam.ui.network.inviteByNumber.model.InviteByNumberModel
import com.civilcam.ui.network.inviteByNumber.model.InviteByNumberRoute
import com.civilcam.ui.network.inviteByNumber.model.InviteByNumberState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class InviteByNumberViewModel :
    ComposeViewModel<InviteByNumberState, InviteByNumberRoute, InviteByNumberActions>() {
    override var _state: MutableStateFlow<InviteByNumberState> =
        MutableStateFlow(InviteByNumberState())

    override fun setInputActions(action: InviteByNumberActions) {
        when (action) {
            InviteByNumberActions.ClickGoBack -> goBack()
            is InviteByNumberActions.SendInvite -> sendInvite(action.phoneNumber)
            InviteByNumberActions.PhoneCleared -> phoneCleared()
        }
    }

    private fun goBack() {
        navigateRoute(InviteByNumberRoute.GoBack)
    }

    private fun phoneCleared() {
        _state.update { it.copy(clearNumber = null) }
    }

    private fun sendInvite(phoneNumber: String) {
        val numbersList = _state.value.data?.invitationList?.toMutableList() ?: mutableListOf()
        numbersList.add("+1${phoneNumber}")
        viewModelScope.launch {
            _state.update {
                it.copy(
                    clearNumber = Unit,
                    data = InviteByNumberModel(invitationList = numbersList.toList())
                )
            }
        }
    }
}


    



