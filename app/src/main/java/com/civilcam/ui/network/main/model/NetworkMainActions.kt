package com.civilcam.ui.network.main.model

import com.civilcam.common.ext.compose.ComposeFragmentActions
import com.civilcam.domain.model.guard.NetworkType

sealed class NetworkMainActions : ComposeFragmentActions {
    object ClickGoMyProfile : NetworkMainActions()
    object ClickGoSettings : NetworkMainActions()


    data class ClickNetworkTypeChange(val alertType: NetworkType) : NetworkMainActions()

    object ClickAddGuardian : NetworkMainActions()

    object ClickRequests : NetworkMainActions()

    data class ClickUser(val userId: Int) : NetworkMainActions()

}
