package com.civilcam.ui.network.contacts.model

import com.civilcam.common.ext.compose.ComposeFragmentState

data class ContactsState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val data: ContactsModel? = null
) : ComposeFragmentState