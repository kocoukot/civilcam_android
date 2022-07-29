package com.civilcam.ui.alerts.history.model

import androidx.annotation.StringRes
import com.civilcam.R

enum class AlertHistoryScreen(@StringRes val screenTitle: Int) {
    HISTORY_LIST(R.string.alerts_history_title),
    HISTORY_DETAIL(R.string.history_detail_title)
}