package com.civilcam.ui.profile.setup.model

import com.civilcam.domainLayer.model.SearchModel
import com.civilcam.domainLayer.model.profile.UserSetupModel
import com.civilcam.ext_features.compose.ComposeFragmentState

data class ProfileSetupState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val profileSetupScreen: ProfileSetupScreen = ProfileSetupScreen.SETUP,
    val showDatePicker: Boolean = false,
    val data: UserSetupModel? = null,
    val searchLocationModel: SearchModel = SearchModel()
) : ComposeFragmentState