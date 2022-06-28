package com.civilcam.ui.network.main

import com.civilcam.common.ext.compose.ComposeViewModel
import com.civilcam.ui.network.main.model.NetworkMainActions
import com.civilcam.ui.network.main.model.NetworkMainRoute
import com.civilcam.ui.network.main.model.NetworkMainState
import kotlinx.coroutines.flow.MutableStateFlow


class NetworkMainViewModel :
    ComposeViewModel<NetworkMainState, NetworkMainRoute, NetworkMainActions>() {
    override var _state: MutableStateFlow<NetworkMainState> = MutableStateFlow(NetworkMainState())

    override fun setInputActions(action: NetworkMainActions) {
        when (action) {
            NetworkMainActions.ClickGoMyProfile -> TODO()
            NetworkMainActions.ClickGoSettings -> goSettings()
        }
    }

    private fun goSettings() {
        _steps.value = NetworkMainRoute.GoSettings
    }
}


    



