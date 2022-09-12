package com.civilcam.alert_feature.history.model

import androidx.annotation.StringRes
import com.civilcam.alert_feature.R

enum class AlertHistoryScreen(@StringRes val screenTitle: Int) {
    HISTORY_LIST(R.string.alerts_history_title),
    HISTORY_DETAIL(R.string.history_detail_title)
}