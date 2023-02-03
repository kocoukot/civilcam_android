package com.civilcam.ui.network.main.model

import androidx.annotation.StringRes
import com.civilcam.R

enum class NetworkScreen(@StringRes val screenTitle: Int) {
    MAIN(R.string.navigation_bar_network),
    REQUESTS(R.string.network_main_requests),
    SEARCH_GUARD(R.string.user_details_title),
    ADD_GUARD(R.string.user_details_title)
}