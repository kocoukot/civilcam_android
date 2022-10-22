package com.civilcam.ui.profile.setup.model

import com.civilcam.domainLayer.model.AutocompletePlace
import com.civilcam.ext_features.compose.ComposeFragmentActions

sealed class ProfileSetupActions : ComposeFragmentActions {
    object ClickGoBack : ProfileSetupActions()
    object ClickSave : ProfileSetupActions()
    object ClickAvatarSelect : ProfileSetupActions()
    object ClickDateSelect : ProfileSetupActions()
    data class ClickSelectDate(val date: Long? = null) : ProfileSetupActions()

    object ClickGoLocationPicker : ProfileSetupActions()


    data class EnterInputData(val dataType: UserInfoDataType, val data: String) :
        ProfileSetupActions()

    data class LocationSearchQuery(val searchQuery: String) : ProfileSetupActions()
    data class ClickAddressSelect(val address: AutocompletePlace) : ProfileSetupActions()
    object ClickCloseAlert : ProfileSetupActions()

}
