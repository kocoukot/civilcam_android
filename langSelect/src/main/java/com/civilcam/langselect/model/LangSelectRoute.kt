package com.civilcam.langselect.model

import com.civilcam.ext_features.arch.ComposeRouteNavigation
import com.civilcam.ext_features.compose.ComposeFragmentRoute

sealed class LangSelectRoute : ComposeFragmentRoute {
    object ToOnBoarding : LangSelectRoute() {
        override fun handleRoute(): ComposeRouteNavigation =
            ComposeRouteNavigation.DeepLinkNavigate(destination = com.civilcam.ext_features.R.string.direction_onBoardingFragment)
    }
}