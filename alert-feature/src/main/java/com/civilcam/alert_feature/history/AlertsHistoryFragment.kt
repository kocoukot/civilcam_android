package com.civilcam.alert_feature.history

import androidx.compose.runtime.Composable
import com.civilcam.ext_features.arch.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class AlertsHistoryFragment : BaseFragment<AlertsHistoryViewModel>() {
    override val viewModel: AlertsHistoryViewModel by viewModel()

    override val screenContent: @Composable (AlertsHistoryViewModel) -> Unit =
        { AlertsListScreenContent(viewModel) }
}