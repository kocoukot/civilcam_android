package com.civilcam.ui.profile.userDetails.model

data class UserDetailsState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val data: UserDetailsModel? = null,
    val alertType: StopGuardAlertType? = null
)