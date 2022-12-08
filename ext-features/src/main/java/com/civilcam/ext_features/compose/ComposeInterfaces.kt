package com.civilcam.ext_features.compose

import com.civilcam.ext_features.arch.ComposeRouteNavigation

interface ComposeFragmentActions

interface ComposeFragmentState

interface ComposeFragmentRoute {
    fun handleRoute(): ComposeRouteNavigation = ComposeRouteNavigation.CustomRoute
}