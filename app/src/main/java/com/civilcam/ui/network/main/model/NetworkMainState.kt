package com.civilcam.ui.network.main.model

import com.civilcam.common.ext.compose.ComposeFragmentState
import com.civilcam.domainLayer.model.guard.NetworkType

data class NetworkMainState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val refreshList: Unit? = null,
    val screenState: NetworkScreen = NetworkScreen.MAIN,
    val networkType: NetworkType = NetworkType.ON_GUARD,
    val data: NetworkMainModel = NetworkMainModel()
) : ComposeFragmentState