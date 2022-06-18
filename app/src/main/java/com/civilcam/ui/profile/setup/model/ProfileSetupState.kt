package com.civilcam.ui.profile.setup.model

import com.civilcam.domain.model.UserSetupModel

data class ProfileSetupState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val data: UserSetupModel? = null,
    val birthDate: Long? = null
)