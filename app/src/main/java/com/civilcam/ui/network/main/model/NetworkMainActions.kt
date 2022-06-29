package com.civilcam.ui.network.main.model

import com.civilcam.common.ext.compose.ComposeFragmentActions
import com.civilcam.domain.model.guard.NetworkType

sealed class NetworkMainActions : ComposeFragmentActions {
    object ClickGoMyProfile : NetworkMainActions()
    object ClickGoSettings : NetworkMainActions()
    object ClickGoContacts : NetworkMainActions()


    data class ClickNetworkTypeChange(val networkType: NetworkType) : NetworkMainActions()

    object ClickAddGuardian : NetworkMainActions()

    object ClickGoRequests : NetworkMainActions()

    data class ClickUser(val user: GuardianItem) : NetworkMainActions()

    object ClickGoBack : NetworkMainActions()

    object ClickGoSearch : NetworkMainActions()

}
