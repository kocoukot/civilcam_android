package com.civilcam.ui.profile.setup.model

sealed class ProfileSetupActions {
    object CLickGoBack : ProfileSetupActions()
    object ClickSave : ProfileSetupActions()
    object ClickAvatarSelect : ProfileSetupActions()
    object ClickDateSelect : ProfileSetupActions()
    object ClickLocationSelect : ProfileSetupActions()
    data class EnterInputData(val dataType: InputDataType, val data: String) : ProfileSetupActions()
}
