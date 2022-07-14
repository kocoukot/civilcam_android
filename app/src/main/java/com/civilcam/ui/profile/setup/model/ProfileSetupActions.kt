package com.civilcam.ui.profile.setup.model

import com.civilcam.common.ext.compose.ComposeFragmentActions
import com.civilcam.domain.model.AutocompletePlace

sealed class ProfileSetupActions : ComposeFragmentActions {
    object ClickGoBack : ProfileSetupActions()
    object ClickSave : ProfileSetupActions()
    object ClickAvatarSelect : ProfileSetupActions()
    object ClickDateSelect : ProfileSetupActions()
    object ClickCloseDatePicker : ProfileSetupActions()
    data class ClickSelectDate(val date: Long) : ProfileSetupActions()

    object ClickGoLocationPicker : ProfileSetupActions()


    data class EnterInputData(val dataType: UserInfoDataType, val data: String) :
        ProfileSetupActions()

    data class LocationSearchQuery(val searchQuery: String) : ProfileSetupActions()
    data class ClickAddressSelect(val address: AutocompletePlace) : ProfileSetupActions()
}
