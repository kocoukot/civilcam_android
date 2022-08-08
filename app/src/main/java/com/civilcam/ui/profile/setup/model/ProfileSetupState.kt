package com.civilcam.ui.profile.setup.model

import com.civilcam.common.ext.compose.ComposeFragmentState

data class ProfileSetupState(
    val isLoading: Boolean = false,
    var errorText: String = "",
    val profileSetupScreen: ProfileSetupScreen = ProfileSetupScreen.SETUP,
    val showDatePicker: Boolean = false,
    val data: com.civilcam.domainLayer.model.UserSetupModel? = null,
    val birthDate: Long? = null,
    val searchLocationModel: com.civilcam.domainLayer.model.SearchModel = com.civilcam.domainLayer.model.SearchModel()
) : ComposeFragmentState