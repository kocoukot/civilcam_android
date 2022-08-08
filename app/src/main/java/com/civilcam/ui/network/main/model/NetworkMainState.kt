package com.civilcam.ui.network.main.model

import com.civilcam.common.ext.compose.ComposeFragmentState

data class NetworkMainState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val screenState: NetworkScreen = NetworkScreen.MAIN,
    val networkType: com.civilcam.domainLayer.model.guard.NetworkType = com.civilcam.domainLayer.model.guard.NetworkType.ON_GUARD,
    val data: NetworkMainModel? = null
) : ComposeFragmentState