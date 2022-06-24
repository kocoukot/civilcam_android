package com.civilcam.ui.profile.setup.model

import com.civilcam.common.ext.compose.ComposeFragmentActions

sealed class ProfileSetupActions : ComposeFragmentActions {
    object ClickGoBack : ProfileSetupActions()
    object ClickSave : ProfileSetupActions()
    object ClickAvatarSelect : ProfileSetupActions()
    object ClickDateSelect : ProfileSetupActions()
    object ClickLocationSelect : ProfileSetupActions()
    object ClickCloseDatePicker : ProfileSetupActions()
    data class ClickSelectDate(val date: Long) : ProfileSetupActions()

    data class EnterInputData(val dataType: InputDataType, val data: String) : ProfileSetupActions()
}
