package com.civilcam.domainLayer

import androidx.annotation.StringRes

enum class EmergencyScreen(@StringRes val title: Int = 0) {
    NORMAL(R.string.emergency_map_title),
    COUPLED(R.string.emergency_map_title),
    MAP_EXTENDED(R.string.emergency_map_title),
    LIVE_EXTENDED(R.string.emergency_live_stream_title)
}