package com.civilcam.ui.network.main.model

import com.civilcam.common.ext.compose.ComposeFragmentActions
import com.civilcam.domainLayer.model.guard.GuardianItem

sealed class NetworkMainActions : ComposeFragmentActions {
    object ClickGoMyProfile : NetworkMainActions()
    object ClickGoSettings : NetworkMainActions()
    object ClickGoContacts : NetworkMainActions()


    data class ClickNetworkTypeChange(val networkType: com.civilcam.domainLayer.model.guard.NetworkType) : NetworkMainActions()

    object ClickAddGuardian : NetworkMainActions()

    object ClickGoRequests : NetworkMainActions()

    data class ClickUser(val user: GuardianItem) : NetworkMainActions()

    object ClickGoBack : NetworkMainActions()

    object ClickGoSearch : NetworkMainActions()

    data class EnteredSearchString(val searchQuery: String) : NetworkMainActions()
    data class ClickAddUser(val user: com.civilcam.domainLayer.model.guard.GuardianModel) : NetworkMainActions()

}
