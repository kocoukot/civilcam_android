package com.civilcam.ui.network.main.model

import com.civilcam.common.ext.compose.ComposeFragmentRoute

sealed class NetworkMainRoute : ComposeFragmentRoute {
    object GoSettings : NetworkMainRoute()
    object GoContacts : NetworkMainRoute()
    object GoProfile : NetworkMainRoute()
    data class GoUserDetail(val userId: Int) : NetworkMainRoute()
    data class IsNavBarVisible(val isVisible: Boolean) : NetworkMainRoute()


}