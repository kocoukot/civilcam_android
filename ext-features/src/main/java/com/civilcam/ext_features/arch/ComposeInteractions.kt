package com.civilcam.ext_features.arch

import android.os.Bundle
import kotlinx.coroutines.flow.StateFlow


interface ComposeActions


interface ComposeRoute {
    object OnBack : ComposeRoute
}

interface ComposeRouteNavigation {
    interface DeepLinkNavigate : ComposeRouteNavigation {
        val destination: Int
        val arguments: String
            get() = ""

    }

    interface GraphNavigate : ComposeRouteNavigation {
        val destination: Int
        val bundle: Bundle?
            get() = null
    }

    interface PopNavigation : ComposeRouteNavigation

    interface NavigateToStart : ComposeRouteNavigation
}

interface ComposeRouteFinishApp

interface ComposeRoutePermission

interface ComposeRouteCallNumber {
    val phoneNumber: String
}


interface ComposeState

interface StateCommunication<T> {
    val state: StateFlow<T>
    fun updateInfo(info: suspend T.() -> T)
}

