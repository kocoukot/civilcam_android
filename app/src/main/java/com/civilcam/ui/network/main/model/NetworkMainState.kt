package com.civilcam.ui.network.main.model

import com.civilcam.common.ext.compose.ComposeFragmentState

data class NetworkMainState(
    val isLoading: Boolean = false,
    val errorText: String = "",

    ) : ComposeFragmentState