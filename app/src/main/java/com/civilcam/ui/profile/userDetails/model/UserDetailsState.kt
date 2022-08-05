package com.civilcam.ui.profile.userDetails.model

import com.civilcam.common.ext.compose.ComposeFragmentState

data class UserDetailsState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val data: UserDetailsModel? = null,
    val alertType: StopGuardAlertType? = null
) : ComposeFragmentState