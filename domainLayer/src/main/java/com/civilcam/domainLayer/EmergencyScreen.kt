package com.civilcam.domainLayer

import androidx.annotation.StringRes

enum class EmergencyScreen(@StringRes val title: Int, @StringRes val alertTitle: Int = 0) {
    NORMAL(R.string.emergency_map_title, R.string.alert_map_title_user_in_danger),
    COUPLED(R.string.emergency_map_title, R.string.alert_map_title_user_in_danger),
    MAP_EXTENDED(R.string.emergency_map_title, R.string.emergency_map_title),
    LIVE_EXTENDED(R.string.emergency_live_stream_title, R.string.emergency_live_stream_title)
}