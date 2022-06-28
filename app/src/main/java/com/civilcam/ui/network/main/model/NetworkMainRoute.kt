package com.civilcam.ui.network.main.model

import com.civilcam.common.ext.compose.ComposeFragmentRoute

sealed class NetworkMainRoute : ComposeFragmentRoute {
    object GoSettings : NetworkMainRoute()
}