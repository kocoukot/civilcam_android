package com.civilcam.ui.profile.setup.model

import com.civilcam.common.ext.compose.ComposeFragmentState
import com.civilcam.domain.model.SearchModel
import com.civilcam.domain.model.UserSetupModel

data class ProfileSetupState(
    val isLoading: Boolean = false,
    var errorText: String = "",
    val profileSetupScreen: ProfileSetupScreen = ProfileSetupScreen.SETUP,
    val showDatePicker: Boolean = false,
    val data: UserSetupModel? = null,
    val birthDate: Long? = null,
    val searchLocationModel: SearchModel = SearchModel()
) : ComposeFragmentState