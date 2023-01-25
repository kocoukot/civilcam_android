package com.civilcam.ui.network.inviteByNumber

import androidx.lifecycle.viewModelScope
import com.civilcam.domainLayer.model.guard.GuardianStatus
import com.civilcam.domainLayer.usecase.guardians.GetPhoneInvitesUseCase
import com.civilcam.domainLayer.usecase.guardians.InviteByNumberUseCase
import com.civilcam.ext_features.arch.BaseViewModel
import com.civilcam.ext_features.compose.ComposeFragmentActions
import com.civilcam.ext_features.ext.clearPhone
import com.civilcam.ui.network.inviteByNumber.model.InviteByNumberActions
import com.civilcam.ui.network.inviteByNumber.model.InviteByNumberModel
import com.civilcam.ui.network.inviteByNumber.model.InviteByNumberRoute
import com.civilcam.ui.network.inviteByNumber.model.InviteByNumberState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class InviteByNumberViewModel(
    private val inviteByNumberUseCase: InviteByNumberUseCase,
    private val getPhoneInvitesUseCase: GetPhoneInvitesUseCase
) : BaseViewModel.Base<InviteByNumberState>(
    mState = MutableStateFlow(InviteByNumberState())
) {

    init {
        updateInfo { copy(isLoading = true) }
        networkRequest(
            action = { getPhoneInvitesUseCase() },
            onSuccess = { list ->
                updateInfo {
                    copy(data =
                    InviteByNumberModel(
                        list.filter { contact -> contact.status == GuardianStatus.PENDING }
                            .map { contact -> "+${contact.phone.clearPhone()}" })
                    )
                }
            },
            onFailure = { error -> updateInfo { copy(errorText = error) } },
            onComplete = { updateInfo { copy(isLoading = false) } },
        )
    }


    override fun setInputActions(action: ComposeFragmentActions) {
        when (action) {
            InviteByNumberActions.ClickGoBack -> goBack()
            is InviteByNumberActions.SendInvite -> sendInvite()
            InviteByNumberActions.PhoneCleared -> phoneCleared()
            InviteByNumberActions.ClickCloseScreenAlert -> clearErrorText()
            is InviteByNumberActions.PhoneEntered -> phoneEntered(action.phoneNumber)
        }
    }

    private fun goBack() {
        sendRoute(InviteByNumberRoute.GoBack)
    }

    private fun phoneCleared() {
        updateInfo { copy(clearNumber = null) }
    }

    private fun phoneEntered(phone: String) {
        updateInfo { copy(phoneNumber = phone) }
    }

    override fun clearErrorText() {
        updateInfo { copy(errorText = "") }
    }

    private fun sendInvite() {
        updateInfo { copy(isLoading = true) }
        viewModelScope.launch {
            networkRequest(
                action = { inviteByNumberUseCase("+1${getState().phoneNumber}") },
                onSuccess = {
                    val numbersList =
                        getState().data?.invitationList?.toMutableList() ?: mutableListOf()
                    numbersList.add("+1${getState().phoneNumber}")
                    updateInfo {
                        copy(
                            clearNumber = Unit,
                            phoneNumber = "",
                            data = InviteByNumberModel(invitationList = numbersList.toList())
                        )
                    }
                },
                onFailure = { error -> updateInfo { copy(errorText = error) } },
                onComplete = { updateInfo { copy(isLoading = false) } },
            )

        }
    }
}