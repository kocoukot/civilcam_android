package com.civilcam.ui.network.main.model

import com.civilcam.common.ext.compose.ComposeFragmentState
import com.civilcam.domain.model.guard.NetworkType

data class NetworkMainState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val screenState: NetworkScreen = NetworkScreen.MAIN,

    val networkType: NetworkType = NetworkType.ON_GUARD,
    val needToLoadMock: Boolean = false,
    val data: NetworkMainModel? = null
) : ComposeFragmentState