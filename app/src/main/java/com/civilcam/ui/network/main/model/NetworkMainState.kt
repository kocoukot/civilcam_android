package com.civilcam.ui.network.main.model

import com.civilcam.common.ext.compose.ComposeFragmentState
import com.civilcam.domainLayer.model.guard.NetworkType
import com.civilcam.domainLayer.model.user.ImageInfo

data class NetworkMainState(
    var isLoading: Boolean = false,
    val errorText: String = "",
    val refreshList: Unit? = null,
    val userAvatar: ImageInfo? = null,
    val screenState: NetworkScreen = NetworkScreen.MAIN,
    val networkType: NetworkType = NetworkType.ON_GUARD,
    val data: NetworkMainModel = NetworkMainModel()
) : ComposeFragmentState