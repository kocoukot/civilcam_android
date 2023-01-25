package com.civilcam.ext_features.compose

import androidx.fragment.app.Fragment
import com.civilcam.ext_features.arch.ComposeRouteNavigation
import com.civilcam.ext_features.arch.EndSubscription

interface ComposeFragmentActions

interface ComposeFragmentState

interface ComposeFragmentRoute {
    fun handleRoute(): ComposeRouteNavigation = ComposeRouteNavigation.CustomRoute

    object SubEnd : ComposeFragmentRoute, ComposeRouteNavigation {
        override fun handleNavigation(fragment: Fragment, block: (() -> Unit)?) {
            (fragment.requireActivity() as EndSubscription).subscriptionEnd()
        }
    }
}