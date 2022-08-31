package com.civilcam.ui.profile.userDetails.model

import com.civilcam.common.ext.compose.ComposeFragmentState
import com.civilcam.domainLayer.model.UserDetailsModel

data class UserDetailsState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val data: UserDetailsModel = UserDetailsModel(),
    val alertType: StopGuardAlertType? = null
) : ComposeFragmentState