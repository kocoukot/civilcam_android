package com.civilcam.ui.network.inviteByNumber.model

import com.civilcam.ext_features.compose.ComposeFragmentState

data class InviteByNumberState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val clearNumber: Unit? = null,
    val phoneNumber: String = "",
    val data: InviteByNumberModel? = null
) : ComposeFragmentState