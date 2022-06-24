package com.civilcam.ui.network.inviteByNumber.model

import com.civilcam.common.ext.compose.ComposeFragmentState

data class InviteByNumberState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val clearNumber: Unit? = null,
    val data: InviteByNumberModel? = null
) : ComposeFragmentState