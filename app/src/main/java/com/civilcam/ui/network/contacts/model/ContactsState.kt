package com.civilcam.ui.network.contacts.model

import androidx.compose.runtime.Composable
import com.civilcam.domainLayer.model.guard.UserInviteModel
import com.civilcam.ext_features.compose.ComposeFragmentState
import com.civilcam.ext_features.compose.elements.DialogLoadingContent

data class ContactsState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val data: ContactsModel? = null,
    val invitesList: List<UserInviteModel> = emptyList()
) : ComposeFragmentState {

    @Composable
    fun checkIsLoading() = if (isLoading) DialogLoadingContent() else null
}