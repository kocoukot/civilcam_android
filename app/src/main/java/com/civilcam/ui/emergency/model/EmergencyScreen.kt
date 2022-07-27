package com.civilcam.ui.emergency.model

import androidx.annotation.StringRes
import com.civilcam.R

enum class EmergencyScreen(@StringRes val title: Int = 0) {
    NORMAL(R.string.emergency_map_title),
    COUPLED(R.string.emergency_map_title),
    MAP_EXTENDED(R.string.emergency_map_title),
    LIVE_EXTENDED(R.string.emergency_live_stream_title)
}